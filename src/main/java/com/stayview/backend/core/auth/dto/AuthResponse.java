package com.stayview.backend.core.auth.dto;

import com.stayview.backend.domain.user.dto.UserResponse;

public record AuthResponse(
	String loginType,
	String accessMode,
	UserResponse user
) {
}
