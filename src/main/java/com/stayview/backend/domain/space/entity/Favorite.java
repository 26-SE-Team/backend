package com.stayview.backend.domain.space.entity;

import com.stayview.backend.common.BaseTimeEntity;
import com.stayview.backend.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "favorites")
public class Favorite extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "favorite_id")
	private Long favoriteId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "space_id", nullable = false)
	private Space space;

	protected Favorite() {
	}

	public Favorite(User user, Space space) {
		this.user = user;
		this.space = space;
	}

	public Long getFavoriteId() {
		return favoriteId;
	}

	public User getUser() {
		return user;
	}

	public Space getSpace() {
		return space;
	}
}
