package com.stayview.backend.domain.agent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AgentProfileRequest(
	@NotBlank
	@Size(max = 100)
	String licenseNo
) {
}
