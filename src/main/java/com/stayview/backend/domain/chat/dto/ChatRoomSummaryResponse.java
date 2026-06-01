package com.stayview.backend.chat.dto;

import com.stayview.backend.chat.entity.ChatMessage;
import com.stayview.backend.chat.entity.ChatRoom;
import java.time.Instant;

public record ChatRoomSummaryResponse(
	Long chatRoomId,
	Long spaceId,
	String spaceTitle,
	Long tenantId,
	String tenantName,
	Long agentId,
	String agentName,
	String lastMessage,
	Instant lastSentAt
) {

	public static ChatRoomSummaryResponse from(ChatRoom chatRoom, ChatMessage lastMessage) {
		return new ChatRoomSummaryResponse(
			chatRoom.getChatRoomId(),
			chatRoom.getSpace().getSpaceId(),
			chatRoom.getSpace().getTitle(),
			chatRoom.getTenant().getUserId(),
			chatRoom.getTenant().getName(),
			chatRoom.getSpace().getAgent().getUserId(),
			chatRoom.getSpace().getAgent().getName(),
			lastMessage == null ? null : lastMessage.getMessage(),
			lastMessage == null ? null : lastMessage.getSentAt()
		);
	}
}
