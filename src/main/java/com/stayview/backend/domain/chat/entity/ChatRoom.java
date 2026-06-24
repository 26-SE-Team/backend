package com.stayview.backend.domain.chat.entity;

import com.stayview.backend.core.common.BaseTimeEntity;
import com.stayview.backend.domain.space.entity.Space;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "chat_rooms")
public class ChatRoom extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_room_id")
	private Long chatRoomId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "space_id", nullable = false)
	private Space space;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tenant_id", nullable = false)
	private User tenant;

	protected ChatRoom() {
	}

	public Long getChatRoomId() {
		return chatRoomId;
	}

	public Space getSpace() {
		return space;
	}

	public User getTenant() {
		return tenant;
	}

	public boolean hasParticipant(Long userId) {
		return tenant.getUserId().equals(userId) || space.getAgent().getUserId().equals(userId);
	}
}
