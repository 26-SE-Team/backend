package com.stayview.backend.space.repository;

import com.stayview.backend.space.entity.SpaceReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceReportRepository extends JpaRepository<SpaceReport, Long> {
}
