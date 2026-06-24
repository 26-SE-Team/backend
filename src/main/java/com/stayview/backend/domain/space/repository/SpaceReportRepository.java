package com.stayview.backend.domain.space.repository;

import com.stayview.backend.domain.space.entity.SpaceReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceReportRepository extends JpaRepository<SpaceReport, Long> {
}
