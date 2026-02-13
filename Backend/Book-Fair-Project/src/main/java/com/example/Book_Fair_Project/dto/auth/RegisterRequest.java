package com.example.Book_Fair_Project.dto.auth;

import com.example.Book_Fair_Project.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank
    private String name;

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 6)
    private String password;

    @NotBlank
    private String confirmPassword;   // ✅ THIS MUST EXIST

    private String businessName;

    private User.Role role;

    // ===== GETTERS & SETTERS =====

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() {   // ✅ THIS METHOD
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {  // ✅ AND THIS
        this.confirmPassword = confirmPassword;
    }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public User.Role getRole() { return role; }
    public void setRole(User.Role role) { this.role = role; }
}
