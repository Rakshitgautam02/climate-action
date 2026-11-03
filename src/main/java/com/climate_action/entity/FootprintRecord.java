package com.climate_action.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "footprint_records")
public class FootprintRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "week_start", nullable = false)
    private LocalDate weekStart;

    private Double electricity;
    private Double carDistance;
    private Double motorcycleDistance;
    private Double busDistance;

    private Double electricityEmissions;
    private Double carEmissions;
    private Double motorcycleEmissions;
    private Double busEmissions;

    private Double totalEmissions;

    public FootprintRecord() {
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(LocalDate weekStart) {
        this.weekStart = weekStart;
    }

    public Double getElectricity() {
        return electricity;
    }

    public void setElectricity(Double electricity) {
        this.electricity = electricity;
    }

    public Double getCarDistance() {
        return carDistance;
    }

    public void setCarDistance(Double carDistance) {
        this.carDistance = carDistance;
    }

    public Double getMotorcycleDistance() {
        return motorcycleDistance;
    }

    public void setMotorcycleDistance(Double motorcycleDistance) {
        this.motorcycleDistance = motorcycleDistance;
    }

    public Double getBusDistance() {
        return busDistance;
    }

    public void setBusDistance(Double busDistance) {
        this.busDistance = busDistance;
    }

    public Double getElectricityEmissions() {
        return electricityEmissions;
    }

    public void setElectricityEmissions(Double electricityEmissions) {
        this.electricityEmissions = electricityEmissions;
    }

    public Double getCarEmissions() {
        return carEmissions;
    }

    public void setCarEmissions(Double carEmissions) {
        this.carEmissions = carEmissions;
    }

    public Double getMotorcycleEmissions() {
        return motorcycleEmissions;
    }

    public void setMotorcycleEmissions(Double motorcycleEmissions) {
        this.motorcycleEmissions = motorcycleEmissions;
    }

    public Double getBusEmissions() {
        return busEmissions;
    }

    public void setBusEmissions(Double busEmissions) {
        this.busEmissions = busEmissions;
    }

    public Double getTotalEmissions() {
        return totalEmissions;
    }

    public void setTotalEmissions(Double totalEmissions) {
        this.totalEmissions = totalEmissions;
    }
}