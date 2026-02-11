package com.example.Book_Fair_Project.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    public enum Role {
        USER,
        ADMIN,
        VENDOR,
        PUBLISHER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "business_name", length = 100)
    private String businessName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;

    @Column(name = "is_email_verified", nullable = false)
    private boolean emailVerified = false;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Relationships (based on diagram)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserOtp> otps = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmailNotification> emails = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserGenre> genres = new ArrayList<>();

    public User() {}

    public User(Long userId, String name, String email, String password, String businessName, Role role,
                boolean emailVerified, LocalDateTime createdAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.businessName = businessName;
        this.role = role;
        this.emailVerified = emailVerified;
        this.createdAt = createdAt;
    }

    // Getters & Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean isEmailVerified() { return emailVerified; }
    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<Reservation> getReservations() { return reservations; }
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }

    public List<UserOtp> getOtps() { return otps; }
    public void setOtps(List<UserOtp> otps) { this.otps = otps; }

    public List<EmailNotification> getEmails() { return emails; }
    public void setEmails(List<EmailNotification> emails) { this.emails = emails; }

    public List<UserGenre> getGenres() { return genres; }
    public void setGenres(List<UserGenre> genres) { this.genres = genres; }
}
