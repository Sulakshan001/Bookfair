package com.example.Book_Fair_Project.dto.user;

import jakarta.validation.constraints.Size;

public class UserUpdateRequest {

    @Size(min = 2, max = 100)
    private String name;

    @Size(max = 100)
    private String businessName;

    public UserUpdateRequest() {}

    public UserUpdateRequest(String name, String businessName) {
        this.name = name;
        this.businessName = businessName;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
}

