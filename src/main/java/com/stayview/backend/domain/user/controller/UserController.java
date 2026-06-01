package com.stayview.backend.user.controller;

import com.stayview.backend.common.response.ApiResponse;
import com.stayview.backend.space.dto.SpaceResponse;
import com.stayview.backend.user.dto.UserResponse;
import com.stayview.backend.user.dto.UserUpdateRequest;
import com.stayview.backend.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 현재 로그인한 사용자 프로필을 조회한다.
	 */
	@GetMapping
	public ApiResponse<UserResponse> getMe(@RequestHeader("X-User-Id") Long userId) {
		return ApiResponse.ok(userService.getMyProfile(userId));
	}

	/**
	 * 현재 로그인한 사용자 프로필을 수정한다.
	 */
	@PatchMapping
	public ApiResponse<UserResponse> updateMe(
		@RequestHeader("X-User-Id") Long userId,
		@Valid @RequestBody UserUpdateRequest request
	) {
		return ApiResponse.ok(userService.updateMyProfile(userId, request));
	}

	/**
	 * 참조 무결성을 보존하기 위해 회원 탈퇴는 소프트 삭제로 처리한다.
	 */
	@PatchMapping("/delete")
	public ApiResponse<Void> withdraw(@RequestHeader("X-User-Id") Long userId) {
		userService.withdraw(userId);
		return ApiResponse.ok();
	}

	/**
	 * 현재 사용자가 찜한 공간 목록을 조회한다.
	 */
	@GetMapping("/scraps")
	public ApiResponse<List<SpaceResponse>> getMyScraps(@RequestHeader("X-User-Id") Long userId) {
		return ApiResponse.ok(userService.getMyScraps(userId));
	}
}
