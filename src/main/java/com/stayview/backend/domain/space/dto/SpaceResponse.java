package com.stayview.backend.space.dto;

import com.stayview.backend.space.entity.Space;
import com.stayview.backend.space.entity.SpaceImage;
import com.stayview.backend.space.entity.SpaceStatus;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public record SpaceResponse(
	Long spaceId,
	Long agentId,
	String agentName,
	String title,
	String address,
	Double area,
	Integer deposit,
	Integer monthlyRent,
	Integer maintenanceFee,
	String roomType,
	LocalDate availableDate,
	SpaceStatus status,
	String livingEnvironmentInfo,
	List<String> imageUrls,
	boolean scrapped,
	Instant createdAt,
	Instant updatedAt
) {

	public static SpaceResponse from(Space space, boolean scrapped) {
		List<String> imageUrls = space.getImages()
			.stream()
			.sorted(Comparator.comparingInt(SpaceImage::getImageOrder))
			.map(SpaceImage::getImageUrl)
			.toList();

		return new SpaceResponse(
			space.getSpaceId(),
			space.getAgent().getUserId(),
			space.getAgent().getName(),
			space.getTitle(),
			space.getAddress(),
			space.getArea(),
			space.getDeposit(),
			space.getMonthlyRent(),
			space.getMaintenanceFee(),
			space.getRoomType(),
			space.getAvailableDate(),
			space.getStatus(),
			space.getLivingEnvironmentInfo(),
			imageUrls,
			scrapped,
			space.getCreatedAt(),
			space.getUpdatedAt()
		);
	}
}
