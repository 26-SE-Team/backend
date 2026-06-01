package com.stayview.backend.user.repository;

import com.stayview.backend.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByGoogleEmail(String googleEmail);

	Optional<User> findByKakaoEmail(String kakaoEmail);
}
