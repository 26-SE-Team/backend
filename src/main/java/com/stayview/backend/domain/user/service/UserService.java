package com.stayview.backend.domain.user.service;

import com.stayview.backend.common.error.BusinessException;
import com.stayview.backend.common.error.ErrorCode;
import com.stayview.backend.domain.space.dto.SpaceResponse;
import com.stayview.backend.domain.space.repository.FavoriteRepository;
import com.stayview.backend.domain.user.dto.UserResponse;
import com.stayview.backend.domain.user.dto.UserUpdateRequest;
import com.stayview.backend.domain.user.entity.User;
import com.stayview.backend.domain.user.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;
	private final FavoriteRepository favoriteRepository;

	public UserService(UserRepository userRepository, FavoriteRepository favoriteRepository) {
		this.userRepository = userRepository;
		this.favoriteRepository = favoriteRepository;
	}

	public User getRequiredUser(Long userId) {
		return userRepository.findById(userId)
			.filter(user -> !user.isDeleted())
			.orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "사용자를 찾을 수 없습니다."));
	}

	public UserResponse getMyProfile(Long userId) {
		return UserResponse.from(getRequiredUser(userId));
	}

	@Transactional
	public UserResponse updateMyProfile(Long userId, UserUpdateRequest request) {
		User user = getRequiredUser(userId);
		user.updateProfile(request.name(), request.phone());
		return UserResponse.from(user);
	}

	@Transactional
	public void withdraw(Long userId) {
		User user = getRequiredUser(userId);
		user.withdraw();
	}

	public List<SpaceResponse> getMyScraps(Long userId) {
		getRequiredUser(userId);
		return favoriteRepository.findByUser_UserIdOrderByCreatedAtDesc(userId)
			.stream()
			.map(favorite -> SpaceResponse.from(favorite.getSpace(), true))
			.toList();
	}
}
