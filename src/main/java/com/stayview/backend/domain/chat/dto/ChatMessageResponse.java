package com.stayview.backend.chat.dto;

import com.stayview.backend.chat.entity.ChatMessage;
import java.time.Instant;

public record ChatMessageResponse(
	Long messageId,
	Long senderId,
	String senderName,
	String message,
	Instant sentAt
) {

	public static ChatMessageResponse from(ChatMessage chatMessage) {
		return new ChatMessageResponse(
			chatMessage.getMessageId(),
			chatMessage.getSender().getUserId(),
			chatMessage.getSender().getName(),
			chatMessage.getMessage(),
			chatMessage.getSentAt()
		);
	}
}
