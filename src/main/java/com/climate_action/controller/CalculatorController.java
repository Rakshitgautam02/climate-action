package com.climate_action.controller;

import com.climate_action.dto.CalculatorRequest;
import com.climate_action.entity.EmissionFactor;
import com.climate_action.service.EmissionCalculatorService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CalculatorController {

    private final EmissionCalculatorService service;

    public CalculatorController(EmissionCalculatorService service) {
        this.service = service;
    }

    // Get all emission factors
    @GetMapping("/emission-factors")
    public List<EmissionFactor> getEmissionFactors() {
        return service.getAllEmissionFactors();
    }

    // Calculate carbon footprint
    @PostMapping("/calculate")
    public Map<String, Object> calculateEmissions(
            @Valid @RequestBody CalculatorRequest request) {

        return service.calculateEmissions(request);
    }
}