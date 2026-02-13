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
    @Column(nullable = false)
    private Size size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.AVAILABLE;

    // ✅ Map marker fields
    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @OneToMany(mappedBy = "stall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationStall> reservationStalls = new ArrayList<>();

    public Stall() {}

    public Long getStallId() { return stallId; }
    public void setStallId(Long stallId) { this.stallId = stallId; }

    public String getStallCode() { return stallCode; }
    public void setStallCode(String stallCode) { this.stallCode = stallCode; }

    public Size getSize() { return size; }
    public void setSize(Size size) { this.size = size; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public List<ReservationStall> getReservationStalls() { return reservationStalls; }
    public void setReservationStalls(List<ReservationStall> reservationStalls) { this.reservationStalls = reservationStalls; }
}
