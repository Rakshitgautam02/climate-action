package com.climate_action.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class CalculatorRequest {

    @NotNull(message = "Electricity consumption is required")
    @PositiveOrZero(message = "Electricity consumption cannot be negative")
    private Double electricity;

    @NotNull(message = "Car distance is required")
    @PositiveOrZero(message = "Car distance cannot be negative")
    private Double carDistance;

    @NotNull(message = "Motorcycle distance is required")
    @PositiveOrZero(message = "Motorcycle distance cannot be negative")
    private Double motorcycleDistance;

    @NotNull(message = "Bus distance is required")
    @PositiveOrZero(message = "Bus distance cannot be negative")
    private Double busDistance;

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
}