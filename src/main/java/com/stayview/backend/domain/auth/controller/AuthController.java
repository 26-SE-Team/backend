package com.stayview.backend.domain.auth.controller;

import com.stayview.backend.common.response.ApiResponse;
import com.stayview.backend.domain.auth.dto.AuthResponse;
import com.stayview.backend.domain.auth.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	/**
	 * Google OAuth는 프론트 연동 전이므로 데모 사용자 생성/조회 방식으로 제공한다.
	 */
	@GetMapping("/google")
	public ApiResponse<AuthResponse> googleLogin(
		@RequestParam(defaultValue = "google-demo@stayview.local") String email,
		@RequestParam(defaultValue = "Google Demo User") String name
	) {
		return ApiResponse.ok(authService.loginWithGoogle(email, name));
	}

	/**
	 * Kakao OAuth는 프론트 연동 전이므로 데모 사용자 생성/조회 방식으로 제공한다.
	 */
	@GetMapping("/kakao")
	public ApiResponse<AuthResponse> kakaoLogin(
		@RequestParam(defaultValue = "kakao-demo@stayview.local") String email,
		@RequestParam(defaultValue = "Kakao Demo User") String name
	) {
		return ApiResponse.ok(authService.loginWithKakao(email, name));
	}

	/**
	 * 무상태 API 구조라 서버 세션 제거 없이 클라이언트 토큰 폐기 메시지만 반환한다.
	 */
	@PostMapping("/logout")
	public ApiResponse<Void> logout() {
		return ApiResponse.ok();
	}
}
