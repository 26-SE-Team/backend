package com.stayview.backend.domain.agent.controller;

import com.stayview.backend.core.common.response.ApiResponse;
import com.stayview.backend.domain.agent.dto.AgentProfileRequest;
import com.stayview.backend.domain.agent.dto.AgentProfileResponse;
import com.stayview.backend.domain.agent.service.AgentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/me/agent-profile")
public class AgentController {

	private final AgentService agentService;

	public AgentController(AgentService agentService) {
		this.agentService = agentService;
	}

	/**
	 * 내 중개사 인증 상태를 조회한다.
	 */
	@GetMapping
	public ApiResponse<AgentProfileResponse> getMyAgentProfile(@RequestHeader("X-User-Id") Long userId) {
		return ApiResponse.ok(agentService.getMyProfile(userId));
	}

	/**
	 * 중개사 등록 신청을 생성한다.
	 */
	@PostMapping
	public ResponseEntity<ApiResponse<AgentProfileResponse>> apply(
		@RequestHeader("X-User-Id") Long userId,
		@Valid @RequestBody AgentProfileRequest request
	) {
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(ApiResponse.created(agentService.apply(userId, request)));
	}

	/**
	 * 아직 승인 전인 중개사 등록 신청을 취소한다.
	 */
	@DeleteMapping
	public ApiResponse<Void> cancel(@RequestHeader("X-User-Id") Long userId) {
		agentService.cancel(userId);
		return ApiResponse.ok();
	}
}
