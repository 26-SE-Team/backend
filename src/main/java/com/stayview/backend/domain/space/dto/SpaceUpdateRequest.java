package com.stayview.backend.space.dto;

import com.stayview.backend.space.entity.SpaceStatus;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public record SpaceUpdateRequest(
	@Size(max = 100)
	String title,

	@Size(max = 255)
	String address,

	@PositiveOrZero
	Double area,

	@PositiveOrZero
	Integer deposit,

	@PositiveOrZero
	Integer monthlyRent,

	@PositiveOrZero
	Integer maintenanceFee,

	@Size(max = 50)
	String roomType,

	LocalDate availableDate,

	SpaceStatus status,

	String livingEnvironmentInfo,

	List<@Size(max = 500) String> imageUrls
) {
}
