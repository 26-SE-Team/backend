package com.stayview.backend.domain.space.controller;

import com.stayview.backend.core.common.response.ApiResponse;
import com.stayview.backend.domain.space.dto.SpaceCreateRequest;
import com.stayview.backend.domain.space.dto.SpaceResponse;
import com.stayview.backend.domain.space.dto.SpaceSearchCondition;
import com.stayview.backend.domain.space.dto.SpaceUpdateRequest;
import com.stayview.backend.domain.space.entity.SpaceStatus;
import com.stayview.backend.domain.space.service.SpaceService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/spaces")
public class SpaceController {

	private final SpaceService spaceService;

	public SpaceController(SpaceService spaceService) {
		this.spaceService = spaceService;
	}

	/**
	 * 승인된 중개사가 공간을 등록한다.
	 */
	@PostMapping
	public ResponseEntity<ApiResponse<SpaceResponse>> create(
		@RequestHeader("X-User-Id") Long userId,
		@Valid @RequestBody SpaceCreateRequest request
	) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(ApiResponse.created(spaceService.create(userId, request)));
	}

	/**
	 * 공간 목록 조회와 검색 조건 조회를 하나의 API로 처리한다.
	 */
	@GetMapping
	public ApiResponse<List<SpaceResponse>> search(
		@RequestHeader(value = "X-User-Id", required = false) Long viewerId,
		@RequestParam(required = false) String keyword,
		@RequestParam(required = false) String roomType,
		@RequestParam(required = false) Integer minDeposit,
		@RequestParam(required = false) Integer maxDeposit,
		@RequestParam(required = false) Integer minRent,
		@RequestParam(required = false) Integer maxRent,
		@RequestParam(required = false) SpaceStatus status
	) {
		SpaceSearchCondition condition = new SpaceSearchCondition(
			keyword,
			roomType,
			minDeposit,
			maxDeposit,
			minRent,
			maxRent,
			status
		);
		return ApiResponse.ok(spaceService.search(condition, viewerId));
	}

	/**
	 * 공간 상세 정보를 조회한다.
	 */
	@GetMapping("/{spaceId}")
	public ApiResponse<SpaceResponse> get(
		@RequestHeader(value = "X-User-Id", required = false) Long viewerId,
		@PathVariable Long spaceId
	) {
		return ApiResponse.ok(spaceService.get(spaceId, viewerId));
	}

	/**
	 * 공간 소유 중개사가 공간 정보를 수정한다.
	 */
	@PatchMapping("/{spaceId}")
	public ApiResponse<SpaceResponse> update(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long spaceId,
		@Valid @RequestBody SpaceUpdateRequest request
	) {
		return ApiResponse.ok(spaceService.update(userId, spaceId, request));
	}

	/**
	 * 공간 삭제는 참조 데이터 보존을 위해 상태값 DELETED로 처리한다.
	 */
	@DeleteMapping("/{spaceId}")
	public ApiResponse<Void> delete(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long spaceId
	) {
		spaceService.delete(userId, spaceId);
		return ApiResponse.ok();
	}

	/**
	 * 현재 사용자가 공간을 찜한다.
	 */
	@PostMapping("/{spaceId}/scrap")
	public ApiResponse<Void> scrap(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long spaceId
	) {
		spaceService.scrap(userId, spaceId);
		return ApiResponse.ok();
	}

	/**
	 * 현재 사용자가 공간 찜을 취소한다.
	 */
	@DeleteMapping("/{spaceId}/scrap")
	public ApiResponse<Void> unscrap(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable Long spaceId
	) {
		spaceService.unscrap(userId, spaceId);
		return ApiResponse.ok();
	}
}
