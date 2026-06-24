package com.stayview.backend.domain.agent.dto;

import jakarta.validation.constraints.Size;

public record AgentDecisionRequest(
	@Size(max = 255)
	String rejectionReason
) {
}
