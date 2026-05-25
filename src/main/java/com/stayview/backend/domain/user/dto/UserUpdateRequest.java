package com.stayview.backend.domain.user.dto;

import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
	@Size(max = 50)
	String name,

	@Size(max = 30)
	String phone
) {
}
