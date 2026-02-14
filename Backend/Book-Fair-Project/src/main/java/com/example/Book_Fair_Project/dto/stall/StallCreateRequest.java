package com.example.Book_Fair_Project.dto.stall;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class StallCreateRequest {

    @NotBlank @Size(max = 10)
    private String stallCode;

    @NotNull
    private String size;   // SMALL/MEDIUM/LARGE

    private String status; // AVAILABLE/RESERVED (optional)

    public StallCreateRequest() {}

    public StallCreateRequest(String stallCode, String size, String status) {
        this.stallCode = stallCode;
        this.size = size;
        this.status = status;
    }

    public String getStallCode() { return stallCode; }
    public void setStallCode(String stallCode) { this.stallCode = stallCode; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
