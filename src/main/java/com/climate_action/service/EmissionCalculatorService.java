package com.climate_action.service;

import com.climate_action.dto.CalculatorRequest;
import com.climate_action.entity.EmissionFactor;
import com.climate_action.repository.EmissionFactorRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class EmissionCalculatorService {

    private final EmissionFactorRepository repository;

    public EmissionCalculatorService(EmissionFactorRepository repository) {
        this.repository = repository;
    }

    // Retrieve all emission factors from MySQL
    public List<EmissionFactor> getAllEmissionFactors() {
        return repository.findAll();
    }

    // Calculate carbon emissions
    public Map<String, Object> calculateEmissions(CalculatorRequest request) {

        // Fetch emission factors from MySQL
        List<EmissionFactor> factors = repository.findAll();

        // Store emission factors in a map
        Map<String, EmissionFactor> factorMap = new HashMap<>();

        for (EmissionFactor factor : factors) {
            factorMap.put(factor.getActivityType(), factor);
        }

        // Check whether all required emission factors exist
        String[] requiredFactors = {
                "ELECTRICITY",
                "CAR",
                "MOTORCYCLE",
                "BUS"
        };

        for (String activity : requiredFactors) {
            if (!factorMap.containsKey(activity)) {
                throw new IllegalStateException(
                        "Missing emission factor: " + activity
                );
            }
        }

        // Calculate emissions for each activity
        double electricityEmissions =
                request.getElectricity()
                * factorMap.get("ELECTRICITY").getFactorValue();

        double carEmissions =
                request.getCarDistance()
                * factorMap.get("CAR").getFactorValue();

        double motorcycleEmissions =
                request.getMotorcycleDistance()
                * factorMap.get("MOTORCYCLE").getFactorValue();

        double busEmissions =
                request.getBusDistance()
                * factorMap.get("BUS").getFactorValue();

        // Calculate total emissions
        double totalEmissions =
                electricityEmissions
                + carEmissions
                + motorcycleEmissions
                + busEmissions;

        // Return results
        return Map.of(
                "electricityEmissions", electricityEmissions,
                "carEmissions", carEmissions,
                "motorcycleEmissions", motorcycleEmissions,
                "busEmissions", busEmissions,
                "totalEmissions", totalEmissions,
                "unit", "kg CO2e"
        );
    }
}