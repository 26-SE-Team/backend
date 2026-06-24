package com.stayview.backend.domain.space.entity;

import com.stayview.backend.core.common.BaseTimeEntity;
import com.stayview.backend.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "spaces")
public class Space extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "space_id")
	private Long spaceId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "agent_id", nullable = false)
	private User agent;

	@Column(name = "title", nullable = false, length = 100)
	private String title;

	@Column(name = "address", nullable = false, length = 255)
	private String address;

	@Column(name = "area")
	private Double area;

	@Column(name = "deposit")
	private Integer deposit;

	@Column(name = "monthly_rent")
	private Integer monthlyRent;

	@Column(name = "maintenance_fee")
	private Integer maintenanceFee;

	@Column(name = "room_type", length = 50)
	private String roomType;

	@Column(name = "available_date")
	private LocalDate availableDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 20)
	@Builder.Default
	private SpaceStatus status = SpaceStatus.AVAILABLE;

	@Column(name = "living_environment_info", columnDefinition = "text")
	private String livingEnvironmentInfo;

	@OneToMany(mappedBy = "space", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<SpaceImage> images = new ArrayList<>();

	public Space(User agent, String title, String address, Double area, Integer deposit, Integer monthlyRent, Integer maintenanceFee, String roomType, LocalDate availableDate, String livingEnvironmentInfo) {
		this.agent = agent;
		this.title = title;
		this.address = address;
		this.area = area;
		this.deposit = deposit;
		this.monthlyRent = monthlyRent;
		this.maintenanceFee = maintenanceFee;
		this.roomType = roomType;
		this.availableDate = availableDate;
		this.status = SpaceStatus.AVAILABLE;
		this.livingEnvironmentInfo = livingEnvironmentInfo;
	}

	public void update(String title, String address, Double area, Integer deposit, Integer monthlyRent, Integer maintenanceFee, String roomType, LocalDate availableDate, SpaceStatus status, String livingEnvironmentInfo) {
		this.title = title;
		this.address = address;
		this.area = area;
		this.deposit = deposit;
		this.monthlyRent = monthlyRent;
		this.maintenanceFee = maintenanceFee;
		this.roomType = roomType;
		this.availableDate = availableDate;
		this.status = status;
		this.livingEnvironmentInfo = livingEnvironmentInfo;
	}

	public void replaceImages(List<String> imageUrls) {
		images.clear();
		if (imageUrls == null) {
			return;
		}
		for (int i = 0; i < imageUrls.size(); i++) {
			images.add(new SpaceImage(this, i + 1, imageUrls.get(i)));
		}
	}

	public void delete() {
		this.status = SpaceStatus.DELETED;
	}

	public boolean isOwnedBy(Long userId) {
		return agent.getUserId().equals(userId);
	}
}
