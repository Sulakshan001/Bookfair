package com.example.Book_Fair_Project.service;



import com.example.Book_Fair_Project.config.JwtService;
import com.example.Book_Fair_Project.dto.auth.*;
import com.example.Book_Fair_Project.exception.BadRequestException;
import com.example.Book_Fair_Project.exception.ConflictException;
import com.example.Book_Fair_Project.exception.NotFoundException;
import com.example.Book_Fair_Project.exception.UnauthorizedException;
import com.example.Book_Fair_Project.model.EmailNotification;
import com.example.Book_Fair_Project.model.User;
import com.example.Book_Fair_Project.model.UserOtp;
import com.example.Book_Fair_Project.repository.UserOtpRepository;
import com.example.Book_Fair_Project.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private static final int OTP_EXPIRY_MINUTES = 10;

    private static final int OTP_RATE_LIMIT_WINDOW_MINUTES = 10;
    private static final int OTP_RATE_LIMIT_MAX = 3;

    private final UserRepository userRepository;
    private final UserOtpRepository userOtpRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    private final SecureRandom secureRandom = new SecureRandom();

    public AuthServiceImpl(
            UserRepository userRepository,
            UserOtpRepository userOtpRepository,
            PasswordEncoder passwordEncoder,
            MailService mailService,
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.userOtpRepository = userOtpRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    // ----------------- REGISTER -----------------

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (request == null) throw new BadRequestException("Request body is missing");
        if (isBlank(request.getName())) throw new BadRequestException("Name is required");
        if (isBlank(request.getEmail())) throw new BadRequestException("Email is required");
        if (isBlank(request.getPassword())) throw new BadRequestException("Password is required");
        if (isBlank(request.getConfirmPassword())) throw new BadRequestException("Confirm password is required");

        if (!Objects.equals(request.getPassword(), request.getConfirmPassword())) {
            throw new BadRequestException("Password and confirm password do not match");
        }

        String email = normalizeEmail(request.getEmail());

        User existingUser = userRepository.findByEmail(email).orElse(null);

        if (existingUser != null) {
            if (!"PENDING_REGISTRATION".equals(existingUser.getName())) {
                throw new ConflictException("Email already registered");
            }
        }

        User.Role role = (request.getRole() == null) ? User.Role.VENDOR : request.getRole();

        if (role == User.Role.ADMIN) {
            throw new UnauthorizedException("Cannot self-register as ADMIN");
        }

        if ((role == User.Role.VENDOR || role == User.Role.PUBLISHER) && isBlank(request.getBusinessName())) {
            throw new BadRequestException("Business name is required for VENDOR or PUBLISHER");
        }

        User u = (existingUser != null) ? existingUser : new User();

        u.setName(request.getName().trim());
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(request.getPassword()));
        u.setBusinessName(isBlank(request.getBusinessName()) ? null : request.getBusinessName().trim());
        u.setRole(role);

        if (existingUser == null) {
            u.setEmailVerified(false);
        }

        User saved = userRepository.save(u);

        // ✅ send OTP only if NOT verified
        if (!saved.isEmailVerified()) {
            sendEmailVerificationOtp(saved); // this will throw if mail fails
        }

        return new AuthResponse(null, saved.getRole(), saved.getUserId(), saved.getEmail(), saved.getName());
    }

    // ----------------- LOGIN -----------------

    @Override
    public AuthResponse login(LoginRequest request) {

        if (request == null) throw new BadRequestException("Request body is missing");
        if (isBlank(request.getEmail()) || isBlank(request.getPassword())) {
            throw new BadRequestException("Email and password are required");
        }

        String email = normalizeEmail(request.getEmail());

        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if ("PENDING_REGISTRATION".equals(u.getName())) {
            throw new UnauthorizedException("Registration not complete. Please complete sign up.");
        }

        if (!u.isEmailVerified()) {
            throw new UnauthorizedException("Email not verified. Please verify your email first.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );

        UserDetails ud = userDetailsService.loadUserByUsername(u.getEmail());
        String token = jwtService.generateToken(ud);

        return new AuthResponse(token, u.getRole(), u.getUserId(), u.getEmail(), u.getName());
    }

    // ----------------- VERIFY EMAIL -----------------

    @Override
    public void verifyEmail(VerifyEmailRequest request) {

        if (request == null) throw new BadRequestException("Request body is missing");
        if (isBlank(request.getEmail())) throw new BadRequestException("Email is required");
        if (isBlank(request.getOtp())) throw new BadRequestException("OTP is required");

        String email = normalizeEmail(request.getEmail());

        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (u.isEmailVerified()) return;

        UserOtp otpEntity = userOtpRepository
                .findTopByUser_UserIdAndOtpTypeAndUsedFalseOrderByCreatedAtDesc(
                        u.getUserId(), UserOtp.OtpType.EMAIL_VERIFY)
                .orElseThrow(() -> new NotFoundException("No OTP found or already used"));

        if (otpEntity.getExpiresAt() != null && otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("OTP expired");
        }

        if (!Objects.equals(otpEntity.getOtp(), request.getOtp().trim())) {
            throw new BadRequestException("Invalid OTP");
        }

        otpEntity.setUsed(true);
        userOtpRepository.save(otpEntity);

        u.setEmailVerified(true);
        userRepository.save(u);
    }

    // ----------------- RESEND OTP -----------------

    @Override
    public void resendVerifyOtp(String emailRaw) {

        if (isBlank(emailRaw)) throw new BadRequestException("Email is required");

        String email = normalizeEmail(emailRaw);

        User u = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (u.isEmailVerified()) {
            throw new BadRequestException("Email already verified");
        }

        sendEmailVerificationOtp(u);
    }

    // ----------------- SEND OTP (for signup flow) -----------------

    @Override
    public void sendOtpToEmail(String emailRaw) {

        if (isBlank(emailRaw)) throw new BadRequestException("Email is required");

        String email = normalizeEmail(emailRaw);

        User u = userRepository.findByEmail(email).orElse(null);

        if (u == null) {
            u = new User();
            u.setEmail(email);
            u.setName("PENDING_REGISTRATION");
            u.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            u.setRole(User.Role.VENDOR);
            u.setEmailVerified(false);
            u = userRepository.save(u);
        } else {
            if (u.isEmailVerified()) {
                throw new BadRequestException("Email already verified");
            }
        }

        sendEmailVerificationOtp(u); // will throw if email sending fails
    }

    // ----------------- FORGOT/RESET (not implemented) -----------------

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        if (request == null) throw new BadRequestException("Request body is missing");
        if (isBlank(request.getEmail())) throw new BadRequestException("Email is required");
        throw new BadRequestException("Forgot password flow not implemented yet");
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        if (request == null) throw new BadRequestException("Request body is missing");
        throw new BadRequestException("Reset password flow not implemented yet");
    }

    // ----------------- HELPERS -----------------

    private void sendEmailVerificationOtp(User user) {

        // rate limit
        LocalDateTime after = LocalDateTime.now().minusMinutes(OTP_RATE_LIMIT_WINDOW_MINUTES);
        long count = userOtpRepository.countByUser_UserIdAndOtpTypeAndCreatedAtAfter(
                user.getUserId(), UserOtp.OtpType.EMAIL_VERIFY, after);

        if (count >= OTP_RATE_LIMIT_MAX) {
            throw new BadRequestException("Too many OTP requests. Try later.");
        }

        String otp = generate6DigitOtp();

        String subject = "BookFair - Email Verification OTP";

        // ✅ HTML BODY (center align + styled card)
        String htmlBody = """
        <div style="font-family:Arial,sans-serif;background:#f4f6f9;padding:40px;">
          <div style="max-width:600px;margin:0 auto;background:#ffffff;border-radius:12px;
                      box-shadow:0 6px 18px rgba(0,0,0,0.08);padding:28px;text-align:center;">
            
            <h2 style="margin:0 0 10px;color:#0B4C5F;">BookFairPro Email Verification</h2>
            <p style="margin:0 0 18px;color:#555;font-size:15px;">Hello,</p>
            <p style="margin:0 0 18px;color:#555;font-size:15px;">Your verification OTP is:</p>

            <div style="display:inline-block;background:#0B4C5F;color:#fff;font-size:28px;
                        font-weight:700;letter-spacing:6px;padding:14px 22px;border-radius:10px;">
              %s
            </div>

            <p style="margin:18px 0 0;color:#777;font-size:13px;">
              This OTP will expire in <b>%d minutes</b>.
            </p>
            <p style="margin:10px 0 0;color:#d9534f;font-size:13px;">
              Do not share this OTP with anyone.
            </p>

            <hr style="margin:22px 0;border:none;border-top:1px solid #eee;">
            <p style="margin:0;color:#999;font-size:12px;">BookFairPro</p>
          </div>
        </div>
        """.formatted(otp, OTP_EXPIRY_MINUTES);

        // ✅ SEND EMAIL FIRST (if fails -> exception -> STOP)
        // IMPORTANT: this method must send HTML (MimeMessageHelper.setText(htmlBody, true))
        mailService.sendAndLogHtml(user, null, EmailNotification.EmailType.GENERAL, subject, htmlBody);

        // ✅ SAVE OTP ONLY AFTER EMAIL SENT
        UserOtp entity = new UserOtp();
        entity.setUser(user);
        entity.setOtpType(UserOtp.OtpType.EMAIL_VERIFY);
        entity.setOtp(otp);
        entity.setUsed(false);
        entity.setExpiresAt(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
        userOtpRepository.save(entity);

        System.out.println("OTP SAVED AFTER EMAIL SENT -> " + user.getEmail());
    }


    private String generate6DigitOtp() {
        int n = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(n);
    }

    private String normalizeEmail(String email) {
        if (isBlank(email)) throw new BadRequestException("Email is required");
        return email.trim().toLowerCase();
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
