package com.stayview.backend.domain.agent.controller;

import com.stayview.backend.core.common.response.ApiResponse;
import com.stayview.backend.domain.agent.dto.AgentDecisionRequest;
import com.stayview.backend.domain.agent.dto.AgentProfileResponse;
import com.stayview.backend.domain.agent.entity.VerificationStatus;
import com.stayview.backend.domain.agent.service.AdminAgentService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/agents")
public class AdminController {

	private final AdminAgentService adminAgentService;

	public AdminController(AdminAgentService adminAgentService) {
		this.adminAgentService = adminAgentService;
	}

	/**
	 * 관리자용 중개사 목록 조회 API.
	 */
	@GetMapping
	public ApiResponse<List<AgentProfileResponse>> listAgents(
		@RequestHeader("X-User-Id") Long adminUserId,
		@RequestParam(required = false) VerificationStatus status
	) {
		return ApiResponse.ok(adminAgentService.listAgents(adminUserId, status));
	}

	/**
	 * 관리자용 중개사 상세 조회 API.
	 */
	@GetMapping("/{userId}")
	public ApiResponse<AgentProfileResponse> getAgent(
		@RequestHeader("X-User-Id") Long adminUserId,
		@PathVariable Long userId
	) {
		return ApiResponse.ok(adminAgentService.getAgent(adminUserId, userId));
	}

	/**
	 * 중개사 인증 승인 API.
	 */
	@PatchMapping("/{userId}/approve")
	public ApiResponse<AgentProfileResponse> approve(
		@RequestHeader("X-User-Id") Long adminUserId,
		@PathVariable Long userId
	) {
		return ApiResponse.ok(adminAgentService.approve(adminUserId, userId));
	}

	/**
	 * 중개사 인증 거절 API.
	 */
	@PatchMapping("/{userId}/reject")
	public ApiResponse<AgentProfileResponse> reject(
		@RequestHeader("X-User-Id") Long adminUserId,
		@PathVariable Long userId,
		@Valid @RequestBody(required = false) AgentDecisionRequest request
	) {
		String reason = request == null ? null : request.rejectionReason();
		return ApiResponse.ok(adminAgentService.reject(adminUserId, userId, reason));
	}
}
