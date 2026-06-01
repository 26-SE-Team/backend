package com.stayview.backend.user.entity;

import com.stayview.backend.common.BaseTimeEntity;
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

import java.time.Instant;

@Entity
@Getter
@Builder
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
	private UserRole role = UserRole.USER;

	@Column(name = "is_deleted", nullable = false)
	private boolean deleted = false;

	@Column(name = "deleted_at")
	private Instant deletedAt;

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
