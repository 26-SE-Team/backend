package com.stayview.backend.domain.user.entity;

import com.stayview.backend.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
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

	protected User() {
	}

	private User(SocialType socialType, String email, String name) {
		this.socialType = socialType;
		if (socialType == SocialType.GOOGLE) {
			this.googleEmail = email;
		}
		if (socialType == SocialType.KAKAO) {
			this.kakaoEmail = email;
		}
		this.name = name;
	}

	public static User socialUser(SocialType socialType, String email, String name) {
		return new User(socialType, email, name);
	}

	public Long getUserId() {
		return userId;
	}

	public SocialType getSocialType() {
		return socialType;
	}

	public String getGoogleEmail() {
		return googleEmail;
	}

	public String getKakaoEmail() {
		return kakaoEmail;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public UserRole getRole() {
		return role;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public Instant getDeletedAt() {
		return deletedAt;
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
