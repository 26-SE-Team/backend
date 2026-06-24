package com.stayview.backend.domain.user.entity;

import com.stayview.backend.core.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "social_type", length = 20)
	private SocialType socialType;

	@Column(name = "google_email", length = 100, unique = true)
	private String googleEmail;

	@Column(name = "kakao_email", length = 100, unique = true)
	private String kakaoEmail;

	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@Column(name = "phone", length = 30)
	private String phone;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, length = 20)
	@Builder.Default
	private UserRole role = UserRole.USER;

	@Column(name = "is_deleted", nullable = false)
	@Builder.Default
	private boolean deleted = false;

	@Column(name = "deleted_at")
	private Instant deletedAt;

	public static User socialUser(SocialType socialType, String email, String name) {
		UserBuilder builder = User.builder()
			.socialType(socialType)
			.name(name)
			.role(UserRole.USER)
			.deleted(false);
		if (socialType == SocialType.GOOGLE) {
			builder.googleEmail(email);
		} else if (socialType == SocialType.KAKAO) {
			builder.kakaoEmail(email);
		}
		return builder.build();
	}

	public boolean isDeleted() {
		return deleted;
	}


	public void updateProfile(String name, String phone) {
		if (name != null && !name.isBlank()) {
			this.name = name;
		}
		this.phone = phone;
	}

	public void withdraw() {
		this.deleted = true;
		this.deletedAt = Instant.now();
	}

	public boolean isAdmin() {
		return role == UserRole.ADMIN;
	}
}
