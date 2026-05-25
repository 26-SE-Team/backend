package com.stayview.backend.domain.agent.dto;

import com.stayview.backend.domain.agent.entity.AgentProfile;
import com.stayview.backend.domain.agent.entity.VerificationStatus;
import java.time.Instant;

public record AgentProfileResponse(
	Long userId,
	String userName,
	String licenseNo,
	VerificationStatus verificationStatus,
	String rejectionReason,
	Instant createdAt,
	Instant updatedAt
) {

	public static AgentProfileResponse from(AgentProfile agentProfile) {
		return new AgentProfileResponse(
			agentProfile.getUserId(),
			agentProfile.getUser().getName(),
			agentProfile.getLicenseNo(),
			agentProfile.getVerificationStatus(),
			agentProfile.getRejectionReason(),
			agentProfile.getCreatedAt(),
			agentProfile.getUpdatedAt()
		);
	}
}
