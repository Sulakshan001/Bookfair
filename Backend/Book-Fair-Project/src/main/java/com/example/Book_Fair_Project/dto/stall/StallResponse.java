package com.example.Book_Fair_Project.dto.stall;


public class StallResponse {

    private Long stallId;
    private String stallCode;
    private String size;
    private String status;
    private String hall;
    private Double price;
    private Double areaSqm;

    public StallResponse() {}

    public StallResponse(Long stallId, String stallCode, String size, String status) {
        this.stallId = stallId;
        this.stallCode = stallCode;
        this.size = size;
        this.status = status;
    }

    public StallResponse(Long stallId, String stallCode, String size, String status, String hall, Double price, Double areaSqm) {
        this.stallId = stallId;
        this.stallCode = stallCode;
        this.size = size;
        this.status = status;
        this.hall = hall;
        this.price = price;
        this.areaSqm = areaSqm;
    }

    public Long getStallId() { return stallId; }
    public void setStallId(Long stallId) { this.stallId = stallId; }

    public String getStallCode() { return stallCode; }
    public void setStallCode(String stallCode) { this.stallCode = stallCode; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getHall() { return hall; }
    public void setHall(String hall) { this.hall = hall; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getAreaSqm() { return areaSqm; }
    public void setAreaSqm(Double areaSqm) { this.areaSqm = areaSqm; }
}