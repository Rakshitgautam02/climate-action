package com.climate_action.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
@Table(name = "emission_factors")
public class EmissionFactor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activity_type")
    private String activityType;

    private String unit;

    @Column(name = "factor_value")
    private Double factorValue;

    private String source;

    public EmissionFactor() {
    }

    public Long getId() {
        return id;
    }

    public String getActivityType() {
        return activityType;
    }

    public String getUnit() {
        return unit;
    }

    public Double getFactorValue() {
        return factorValue;
    }

    public String getSource() {
        return source;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setFactorValue(Double factorValue) {
        this.factorValue = factorValue;
    }

    public void setSource(String source) {
        this.source = source;
    }
}