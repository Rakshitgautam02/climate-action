package com.climate_action.repository;

import com.climate_action.entity.FootprintRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FootprintRecordRepository
        extends JpaRepository<FootprintRecord, Long> {

    List<FootprintRecord> findByUserIdOrderByWeekStartDesc(Long userId);

    Optional<FootprintRecord> findByUserIdAndWeekStart(
            Long userId,
            java.time.LocalDate weekStart
    );
}