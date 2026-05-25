package com.stayview.backend.domain.auth.service;

import com.stayview.backend.domain.auth.dto.AuthResponse;
import com.stayview.backend.domain.user.dto.UserResponse;
import com.stayview.backend.domain.user.entity.SocialType;
import com.stayview.backend.domain.user.entity.User;
import com.stayview.backend.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

	private final UserRepository userRepository;

	public AuthService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional
	public AuthResponse loginWithGoogle(String email, String name) {
		User user = userRepository.findByGoogleEmail(email)
			.orElseGet(() -> userRepository.save(User.socialUser(SocialType.GOOGLE, email, name)));
		return new AuthResponse("GOOGLE", "DEMO_HEADER_X_USER_ID", UserResponse.from(user));
	}

	@Transactional
	public AuthResponse loginWithKakao(String email, String name) {
		User user = userRepository.findByKakaoEmail(email)
			.orElseGet(() -> userRepository.save(User.socialUser(SocialType.KAKAO, email, name)));
		return new AuthResponse("KAKAO", "DEMO_HEADER_X_USER_ID", UserResponse.from(user));
	}
}
