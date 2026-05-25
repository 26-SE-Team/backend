package com.stayview.backend.domain.chat.dto;

import java.util.List;

public record ChatRoomDetailResponse(
	ChatRoomSummaryResponse room,
	List<ChatMessageResponse> messages
) {
}
