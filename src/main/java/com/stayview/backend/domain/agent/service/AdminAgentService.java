package com.stayview.backend.domain.agent.service;

import com.stayview.backend.common.error.BusinessException;
import com.stayview.backend.common.error.ErrorCode;
import com.stayview.backend.domain.agent.dto.AgentProfileResponse;
import com.stayview.backend.domain.agent.entity.AgentProfile;
import com.stayview.backend.domain.agent.entity.VerificationStatus;
import com.stayview.backend.domain.agent.repository.AgentProfileRepository;
import com.stayview.backend.domain.user.entity.User;
import com.stayview.backend.domain.user.service.UserService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminAgentService {

	private final AgentProfileRepository agentProfileRepository;
	private final UserService userService;

	public AdminAgentService(AgentProfileRepository agentProfileRepository, UserService userService) {
		this.agentProfileRepository = agentProfileRepository;
		this.userService = userService;
	}

	public List<AgentProfileResponse> listAgents(Long adminUserId, VerificationStatus status) {
		ensureAdmin(adminUserId);
		List<AgentProfile> profiles = status == null
			? agentProfileRepository.findAllByOrderByCreatedAtDesc()
			: agentProfileRepository.findByVerificationStatusOrderByCreatedAtDesc(status);

		return profiles.stream()
			.map(AgentProfileResponse::from)
			.toList();
	}

	public AgentProfileResponse getAgent(Long adminUserId, Long userId) {
		ensureAdmin(adminUserId);
		return AgentProfileResponse.from(getRequiredProfile(userId));
	}

	@Transactional
	public AgentProfileResponse approve(Long adminUserId, Long userId) {
		ensureAdmin(adminUserId);
		AgentProfile profile = getRequiredProfile(userId);
		profile.approve();
		return AgentProfileResponse.from(profile);
	}

	@Transactional
	public AgentProfileResponse reject(Long adminUserId, Long userId, String reason) {
		ensureAdmin(adminUserId);
		AgentProfile profile = getRequiredProfile(userId);
		profile.reject(reason);
		return AgentProfileResponse.from(profile);
	}

	private void ensureAdmin(Long adminUserId) {
		User admin = userService.getRequiredUser(adminUserId);
		if (!admin.isAdmin()) {
			throw new BusinessException(ErrorCode.FORBIDDEN, "관리자만 사용할 수 있습니다.");
		}
	}

	private AgentProfile getRequiredProfile(Long userId) {
		return agentProfileRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "중개사 프로필을 찾을 수 없습니다."));
	}
}
