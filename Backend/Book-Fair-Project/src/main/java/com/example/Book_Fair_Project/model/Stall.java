package com.example.Book_Fair_Project.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stalls")
public class Stall {

    public enum Size { SMALL, MEDIUM, LARGE }
    public enum Status { AVAILABLE, RESERVED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stall_id")
    private Long stallId;

    @Column(name = "stall_code", nullable = false, length = 10, unique = true)
    private String stallCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "size", nullable = false)
    private Size size;

    @Column(name = "price", nullable = false)
    private Double price = 0.0;

    @Column(name = "area_sqm", nullable = false)
    private Double areaSqm = 0.0;

    @Column(name = "hall", nullable = false, length = 50)
    private String hall;

    // ✅ indoor blueprint map coordinates
    @Column(name = "pos_x", nullable = false)
    private Double posX = 0.0;

    @Column(name = "pos_y", nullable = false)
    private Double posY = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.AVAILABLE;

    @OneToMany(mappedBy = "stall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationStall> reservationStalls = new ArrayList<>();

    public Stall() {}

    // ===== Getters / Setters =====
    public Long getStallId() { return stallId; }
    public void setStallId(Long stallId) { this.stallId = stallId; }

    public String getStallCode() { return stallCode; }
    public void setStallCode(String stallCode) { this.stallCode = stallCode; }

    public Size getSize() { return size; }
    public void setSize(Size size) { this.size = size; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Double getAreaSqm() { return areaSqm; }
    public void setAreaSqm(Double areaSqm) { this.areaSqm = areaSqm; }

    public String getHall() { return hall; }
    public void setHall(String hall) { this.hall = hall; }

    public Double getPosX() { return posX; }
    public void setPosX(Double posX) { this.posX = posX; }

    public Double getPosY() { return posY; }
    public void setPosY(Double posY) { this.posY = posY; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public List<ReservationStall> getReservationStalls() { return reservationStalls; }
    public void setReservationStalls(List<ReservationStall> reservationStalls) {
        this.reservationStalls = reservationStalls;
    }
}