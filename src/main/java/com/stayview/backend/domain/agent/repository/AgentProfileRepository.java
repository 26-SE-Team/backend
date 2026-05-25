package com.stayview.backend.domain.agent.repository;

import com.stayview.backend.domain.agent.entity.AgentProfile;
import com.stayview.backend.domain.agent.entity.VerificationStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentProfileRepository extends JpaRepository<AgentProfile, Long> {

	boolean existsByLicenseNo(String licenseNo);

	List<AgentProfile> findByVerificationStatusOrderByCreatedAtDesc(VerificationStatus verificationStatus);

	List<AgentProfile> findAllByOrderByCreatedAtDesc();
}
