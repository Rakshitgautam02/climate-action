package com.climate_action;

import com.climate_action.entity.EmissionFactor;
import com.climate_action.repository.EmissionFactorRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EmissionFactorDataLoader implements CommandLineRunner {

    private final EmissionFactorRepository repository;

    public EmissionFactorDataLoader(EmissionFactorRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {

        Set<String> existingFactors = repository.findAll()
    .stream()
    .map(factor -> factor.getActivityType())
    .collect(Collectors.toSet());

        List<EmissionFactor> defaults = List.of(

                createFactor(
                        "ELECTRICITY",
                        "kWh",
                        0.675,
                        "Central Electricity Authority, India"
                ),

                createFactor(
                        "CAR",
                        "km",
                        0.209,
                        "Estimated average petrol car"
                ),

                createFactor(
                        "MOTORCYCLE",
                        "km",
                        0.113,
                        "Estimated average motorcycle"
                ),

                createFactor(
                        "BUS",
                        "passenger-km",
                        0.104,
                        "Estimated average bus passenger"
                )
        );

        List<EmissionFactor> missingFactors = defaults.stream()
                .filter(factor ->
                        !existingFactors.contains(factor.getActivityType()))
                .toList();

        if (!missingFactors.isEmpty()) {

            repository.saveAll(missingFactors);

            System.out.println(
                    "Successfully inserted " +
                    missingFactors.size() +
                    " emission factors."
            );

        } else {

            System.out.println("Emission factors already exist.");

        }
    }

    private EmissionFactor createFactor(
            String activity,
            String unit,
            Double value,
            String source) {

        EmissionFactor factor = new EmissionFactor();

        factor.setActivityType(activity);
        factor.setUnit(unit);
        factor.setFactorValue(value);
        factor.setSource(source);

        return factor;
    }
}
