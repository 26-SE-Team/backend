package com.stayview.backend.space.dto;

import com.stayview.backend.space.entity.SpaceStatus;

public record SpaceSearchCondition(
	String keyword,
	String roomType,
	Integer minDeposit,
	Integer maxDeposit,
	Integer minRent,
	Integer maxRent,
	SpaceStatus status
) {
}
