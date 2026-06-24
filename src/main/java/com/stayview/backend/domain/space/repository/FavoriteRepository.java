package com.stayview.backend.domain.space.repository;

import com.stayview.backend.domain.space.entity.Favorite;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

	boolean existsByUser_UserIdAndSpace_SpaceId(Long userId, Long spaceId);

	Optional<Favorite> findByUser_UserIdAndSpace_SpaceId(Long userId, Long spaceId);

	List<Favorite> findByUser_UserIdOrderByCreatedAtDesc(Long userId);
}
