package com.stayview.backend.domain.chat.entity;

import com.stayview.backend.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "chat_messages")
public class ChatMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "message_id")
	private Long messageId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_room_id", nullable = false)
	private ChatRoom chatRoom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id", nullable = false)
	private User sender;

	@Column(name = "message", nullable = false, columnDefinition = "text")
	private String message;

	@Column(name = "sent_at", nullable = false)
	private Instant sentAt;

	protected ChatMessage() {
	}

	public ChatMessage(ChatRoom chatRoom, User sender, String message) {
		this.chatRoom = chatRoom;
		this.sender = sender;
		this.message = message;
	}

	@PrePersist
	void prePersist() {
		if (sentAt == null) {
			sentAt = Instant.now();
		}
	}

	public Long getMessageId() {
		return messageId;
	}

	public ChatRoom getChatRoom() {
		return chatRoom;
	}

	public User getSender() {
		return sender;
	}

	public String getMessage() {
		return message;
	}

	public Instant getSentAt() {
		return sentAt;
	}
}
