package com.climate_action.dto;

import com.climate_action.entity.FootprintRecord;
import java.time.LocalDate;

public class FootprintRecordResponse {

    private Long id;
    private LocalDate weekStart;

    private Double electricityEmissions;
    private Double carEmissions;
    private Double motorcycleEmissions;
    private Double busEmissions;

    private Double totalEmissions;

    public FootprintRecordResponse(FootprintRecord record) {

        this.id = record.getId();
        this.weekStart = record.getWeekStart();

        this.electricityEmissions = record.getElectricityEmissions();
        this.carEmissions = record.getCarEmissions();
        this.motorcycleEmissions = record.getMotorcycleEmissions();
        this.busEmissions = record.getBusEmissions();

        this.totalEmissions = record.getTotalEmissions();
    }

    public Long getId() {
        return id;
    }

    public LocalDate getWeekStart() {
        return weekStart;
    }

    public Double getElectricityEmissions() {
        return electricityEmissions;
    }

    public Double getCarEmissions() {
        return carEmissions;
    }

    public Double getMotorcycleEmissions() {
        return motorcycleEmissions;
    }

    public Double getBusEmissions() {
        return busEmissions;
    }

    public Double getTotalEmissions() {
        return totalEmissions;
    }
}