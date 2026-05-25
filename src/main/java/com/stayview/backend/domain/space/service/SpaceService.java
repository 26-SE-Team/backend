package com.stayview.backend.domain.space.service;

import com.stayview.backend.common.error.BusinessException;
import com.stayview.backend.common.error.ErrorCode;
import com.stayview.backend.domain.agent.entity.AgentProfile;
import com.stayview.backend.domain.agent.service.AgentService;
import com.stayview.backend.domain.space.dto.SpaceCreateRequest;
import com.stayview.backend.domain.space.dto.SpaceResponse;
import com.stayview.backend.domain.space.dto.SpaceSearchCondition;
import com.stayview.backend.domain.space.dto.SpaceUpdateRequest;
import com.stayview.backend.domain.space.entity.Favorite;
import com.stayview.backend.domain.space.entity.Space;
import com.stayview.backend.domain.space.entity.SpaceReport;
import com.stayview.backend.domain.space.entity.SpaceStatus;
import com.stayview.backend.domain.space.repository.FavoriteRepository;
import com.stayview.backend.domain.space.repository.SpaceReportRepository;
import com.stayview.backend.domain.space.repository.SpaceRepository;
import com.stayview.backend.domain.user.entity.User;
import com.stayview.backend.domain.user.service.UserService;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SpaceService {

	private final SpaceRepository spaceRepository;
	private final SpaceReportRepository spaceReportRepository;
	private final FavoriteRepository favoriteRepository;
	private final AgentService agentService;
	private final UserService userService;

	public SpaceService(
		SpaceRepository spaceRepository,
		SpaceReportRepository spaceReportRepository,
		FavoriteRepository favoriteRepository,
		AgentService agentService,
		UserService userService
	) {
		this.spaceRepository = spaceRepository;
		this.spaceReportRepository = spaceReportRepository;
		this.favoriteRepository = favoriteRepository;
		this.agentService = agentService;
		this.userService = userService;
	}

	@Transactional
	public SpaceResponse create(Long userId, SpaceCreateRequest request) {
		AgentProfile agentProfile = agentService.getApprovedAgentProfile(userId);
		Space space = new Space(
			agentProfile.getUser(),
			request.title(),
			request.address(),
			request.area(),
			request.deposit(),
			request.monthlyRent(),
			request.maintenanceFee(),
			request.roomType(),
			request.availableDate(),
			request.livingEnvironmentInfo()
		);
		space.replaceImages(request.imageUrls());
		Space savedSpace = spaceRepository.save(space);
		spaceReportRepository.save(SpaceReport.registrationReport(savedSpace));
		return SpaceResponse.from(savedSpace, false);
	}

	public List<SpaceResponse> search(SpaceSearchCondition condition, Long viewerId) {
		return spaceRepository.findAll(toSpecification(condition), Sort.by(Sort.Direction.DESC, "createdAt"))
			.stream()
			.map(space -> SpaceResponse.from(space, isScrapped(viewerId, space.getSpaceId())))
			.toList();
	}

	public SpaceResponse get(Long spaceId, Long viewerId) {
		Space space = getActiveSpace(spaceId);
		return SpaceResponse.from(space, isScrapped(viewerId, spaceId));
	}

	@Transactional
	public SpaceResponse update(Long userId, Long spaceId, SpaceUpdateRequest request) {
		Space space = getActiveSpace(spaceId);
		ensureOwner(space, userId);
		space.update(
			request.title(),
			request.address(),
			request.area(),
			request.deposit(),
			request.monthlyRent(),
			request.maintenanceFee(),
			request.roomType(),
			request.availableDate(),
			request.status(),
			request.livingEnvironmentInfo()
		);
		if (request.imageUrls() != null) {
			space.replaceImages(request.imageUrls());
		}
		return SpaceResponse.from(space, isScrapped(userId, spaceId));
	}

	@Transactional
	public void delete(Long userId, Long spaceId) {
		Space space = getActiveSpace(spaceId);
		ensureOwner(space, userId);
		space.delete();
	}

	@Transactional
	public void scrap(Long userId, Long spaceId) {
		User user = userService.getRequiredUser(userId);
		Space space = getActiveSpace(spaceId);
		if (favoriteRepository.existsByUser_UserIdAndSpace_SpaceId(userId, spaceId)) {
			return;
		}
		favoriteRepository.save(new Favorite(user, space));
	}

	@Transactional
	public void unscrap(Long userId, Long spaceId) {
		Favorite favorite = favoriteRepository.findByUser_UserIdAndSpace_SpaceId(userId, spaceId)
			.orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "찜 정보를 찾을 수 없습니다."));
		favoriteRepository.delete(favorite);
	}

	private Space getActiveSpace(Long spaceId) {
		return spaceRepository.findById(spaceId)
			.filter(space -> space.getStatus() != SpaceStatus.DELETED)
			.orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "공간을 찾을 수 없습니다."));
	}

	private void ensureOwner(Space space, Long userId) {
		if (!space.isOwnedBy(userId)) {
			throw new BusinessException(ErrorCode.FORBIDDEN, "공간 소유자만 수정할 수 있습니다.");
		}
	}

	private boolean isScrapped(Long userId, Long spaceId) {
		return userId != null && favoriteRepository.existsByUser_UserIdAndSpace_SpaceId(userId, spaceId);
	}

	private Specification<Space> toSpecification(SpaceSearchCondition condition) {
		return (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(criteriaBuilder.notEqual(root.get("status"), SpaceStatus.DELETED));

			if (condition.status() != null) {
				predicates.add(criteriaBuilder.equal(root.get("status"), condition.status()));
			}
			if (condition.keyword() != null && !condition.keyword().isBlank()) {
				String pattern = "%" + condition.keyword().toLowerCase() + "%";
				predicates.add(criteriaBuilder.or(
					criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern),
					criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), pattern)
				));
			}
			if (condition.roomType() != null && !condition.roomType().isBlank()) {
				predicates.add(criteriaBuilder.equal(root.get("roomType"), condition.roomType()));
			}
			if (condition.minDeposit() != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("deposit"), condition.minDeposit()));
			}
			if (condition.maxDeposit() != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("deposit"), condition.maxDeposit()));
			}
			if (condition.minRent() != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("monthlyRent"), condition.minRent()));
			}
			if (condition.maxRent() != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("monthlyRent"), condition.maxRent()));
			}

			return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
		};
	}
}
