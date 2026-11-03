package com.climate_action.service;

import com.climate_action.dto.CalculatorRequest;
import com.climate_action.entity.FootprintRecord;
import com.climate_action.entity.User;
import com.climate_action.repository.FootprintRecordRepository;
import com.climate_action.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class FootprintService {

    private final FootprintRecordRepository footprintRepository;
    private final UserRepository userRepository;
    private final EmissionCalculatorService calculatorService;

    public FootprintService(
            FootprintRecordRepository footprintRepository,
            UserRepository userRepository,
            EmissionCalculatorService calculatorService) {

        this.footprintRepository = footprintRepository;
        this.userRepository = userRepository;
        this.calculatorService = calculatorService;
    }

    // SAVE OR UPDATE WEEKLY FOOTPRINT
    @Transactional
    public FootprintRecord saveRecord(
            String email,
            CalculatorRequest request) {

        // Find the logged-in user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        // Calculate emissions using the emission factors
        Map<String, Object> emissions =
                calculatorService.calculateEmissions(request);

        // Find Monday of the current week
        LocalDate currentWeek =
                LocalDate.now().with(DayOfWeek.MONDAY);

        // Find existing record for this user and week
        // If no record exists, create a new one
        FootprintRecord record = footprintRepository
                .findByUserIdAndWeekStart(user.getId(), currentWeek)
                .orElseGet(FootprintRecord::new);

        record.setUser(user);
        record.setWeekStart(currentWeek);

        // Save input values
        record.setElectricity(request.getElectricity());
        record.setCarDistance(request.getCarDistance());
        record.setMotorcycleDistance(request.getMotorcycleDistance());
        record.setBusDistance(request.getBusDistance());

        // Save individual emissions
        record.setElectricityEmissions(
                (Double) emissions.get("electricityEmissions"));

        record.setCarEmissions(
                (Double) emissions.get("carEmissions"));

        record.setMotorcycleEmissions(
                (Double) emissions.get("motorcycleEmissions"));

        record.setBusEmissions(
                (Double) emissions.get("busEmissions"));

        // Save total emissions
        record.setTotalEmissions(
                (Double) emissions.get("totalEmissions"));

        // Save new record or update existing record
        return footprintRepository.save(record);
    }

    // GET USER'S FOOTPRINT HISTORY
    public List<FootprintRecord> getUserHistory(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        return footprintRepository
                .findByUserIdOrderByWeekStartDesc(user.getId());
    }
}
