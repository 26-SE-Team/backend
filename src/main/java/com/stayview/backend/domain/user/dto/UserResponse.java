package com.stayview.backend.domain.user.dto;

import com.stayview.backend.domain.user.entity.SocialType;
import com.stayview.backend.domain.user.entity.User;
import com.stayview.backend.domain.user.entity.UserRole;
import java.time.Instant;

public record UserResponse(
	Long userId,
	SocialType socialType,
	String email,
	String name,
	String phone,
	UserRole role,
	boolean deleted,
	Instant createdAt,
	Instant updatedAt
) {

	public static UserResponse from(User user) {
		String email = user.getGoogleEmail() != null ? user.getGoogleEmail() : user.getKakaoEmail();
		return new UserResponse(
			user.getUserId(),
			user.getSocialType(),
			email,
			user.getName(),
			user.getPhone(),
			user.getRole(),
			user.isDeleted(),
			user.getCreatedAt(),
			user.getUpdatedAt()
		);
	}
}
