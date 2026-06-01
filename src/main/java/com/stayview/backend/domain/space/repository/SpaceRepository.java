package com.stayview.backend.space.repository;

import com.stayview.backend.space.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpaceRepository extends JpaRepository<Space, Long>, JpaSpecificationExecutor<Space> {
}
