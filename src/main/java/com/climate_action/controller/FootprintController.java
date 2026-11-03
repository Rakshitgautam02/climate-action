package com.climate_action.controller;

import com.climate_action.dto.CalculatorRequest;
import com.climate_action.dto.FootprintRecordResponse;
import com.climate_action.entity.FootprintRecord;
import com.climate_action.service.FootprintService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/footprints")
@CrossOrigin(origins = "*")
public class FootprintController {

    private final FootprintService footprintService;

    public FootprintController(FootprintService footprintService) {
        this.footprintService = footprintService;
    }

    // Save a carbon footprint calculation
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FootprintRecordResponse saveFootprint(
            @Valid @RequestBody CalculatorRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        FootprintRecord record =
                footprintService.saveRecord(email, request);

        return new FootprintRecordResponse(record);
    }

    // Retrieve the logged-in user's footprint history
    @GetMapping
    public List<FootprintRecordResponse> getFootprintHistory(
            Authentication authentication) {

        String email = authentication.getName();

        return footprintService.getUserHistory(email)
                .stream()
                .map(FootprintRecordResponse::new)
                .toList();
    }
}