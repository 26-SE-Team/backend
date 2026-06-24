package com.stayview.backend.domain.agent.entity;

import com.stayview.backend.core.common.BaseTimeEntity;
import com.stayview.backend.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "agent_profiles")
public class AgentProfile extends BaseTimeEntity {

	@Id
	@Column(name = "user_id")
	private Long userId;

	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "license_no", nullable = false, length = 100, unique = true)
	private String licenseNo;

	@Enumerated(EnumType.STRING)
	@Column(name = "verification_status", nullable = false, length = 20)
	@Builder.Default
	private VerificationStatus verificationStatus = VerificationStatus.PENDING;

	@Column(name = "rejection_reason", length = 255)
	private String rejectionReason;

	public AgentProfile(User user, String licenseNo) {
		this.user = user;
		this.userId = user.getUserId();
		this.licenseNo = licenseNo;
		this.verificationStatus = VerificationStatus.PENDING;
	}

	public void approve() {
		this.verificationStatus = VerificationStatus.APPROVED;
		this.rejectionReason = null;
	}

	public void reject(String rejectionReason) {
		this.verificationStatus = VerificationStatus.REJECTED;
		this.rejectionReason = rejectionReason;
	}

	public boolean isApproved() {
		return verificationStatus == VerificationStatus.APPROVED;
	}
}
