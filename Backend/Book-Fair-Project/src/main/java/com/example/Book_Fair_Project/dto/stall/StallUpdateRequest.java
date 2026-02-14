package com.example.Book_Fair_Project.dto.stall;


public class StallUpdateRequest {
    private String size;   // SMALL/MEDIUM/LARGE
    private String status; // AVAILABLE/RESERVED

    public StallUpdateRequest() {}

    public StallUpdateRequest(String size, String status) {
        this.size = size;
        this.status = status;
    }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
