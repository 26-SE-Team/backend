package com.stayview.backend.agent.service;

import com.stayview.backend.common.error.BusinessException;
import com.stayview.backend.common.error.ErrorCode;
import com.stayview.backend.agent.dto.AgentProfileRequest;
import com.stayview.backend.agent.dto.AgentProfileResponse;
import com.stayview.backend.agent.entity.AgentProfile;
import com.stayview.backend.agent.repository.AgentProfileRepository;
import com.stayview.backend.domain.user.entity.User;
import com.stayview.backend.domain.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AgentService {

	private final AgentProfileRepository agentProfileRepository;
	private final UserService userService;

	public AgentService(AgentProfileRepository agentProfileRepository, UserService userService) {
		this.agentProfileRepository = agentProfileRepository;
		this.userService = userService;
	}

	public AgentProfile getApprovedAgentProfile(Long userId) {
		AgentProfile profile = agentProfileRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.FORBIDDEN, "중개사 등록이 필요합니다."));
		if (!profile.isApproved()) {
			throw new BusinessException(ErrorCode.FORBIDDEN, "승인된 중개사만 사용할 수 있습니다.");
		}
		return profile;
	}

	public AgentProfileResponse getMyProfile(Long userId) {
		return AgentProfileResponse.from(agentProfileRepository.findById(userId)
			.orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "중개사 프로필을 찾을 수 없습니다.")));
	}

	@Transactional
	public AgentProfileResponse apply(Long userId, AgentProfileRequest request) {
		User user = userService.getRequiredUser(userId);
		if (agentProfileRepository.existsById(userId)) {
			throw new BusinessException(ErrorCode.CONFLICT, "이미 중개사 신청 정보가 있습니다.");
		}
		if (agentProfileRepository.existsByLicenseNo(request.licenseNo())) {
			throw new BusinessException(ErrorCode.CONFLICT, "이미 사용 중인 중개사 등록번호입니다.");
		}

		return AgentProfileResponse.from(agentProfileRepository.save(new AgentProfile(user, request.licenseNo())));
	}

	@Transactional
	public void cancel(Long userId) {
		if (!agentProfileRepository.existsById(userId)) {
			throw new BusinessException(ErrorCode.NOT_FOUND, "중개사 프로필을 찾을 수 없습니다.");
		}
		agentProfileRepository.deleteById(userId);
	}
}
