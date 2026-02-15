package com.example.Book_Fair_Project.dto.stall;


public class StallResponse {

    private Long stallId;
    private String stallCode;
    private String size;
    private String status;

    public StallResponse() {}

    public StallResponse(Long stallId, String stallCode, String size, String status) {
        this.stallId = stallId;
        this.stallCode = stallCode;
        this.size = size;
        this.status = status;
    }

    public Long getStallId() { return stallId; }
    public void setStallId(Long stallId) { this.stallId = stallId; }

    public String getStallCode() { return stallCode; }
    public void setStallCode(String stallCode) { this.stallCode = stallCode; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
