package com.stayview.backend.domain.chat.controller;

import com.stayview.backend.core.common.response.ApiResponse;
import com.stayview.backend.domain.chat.dto.ChatMessageRequest;
import com.stayview.backend.domain.chat.dto.ChatMessageResponse;
import com.stayview.backend.domain.chat.dto.ChatRoomDetailResponse;
import com.stayview.backend.domain.chat.dto.ChatRoomSummaryResponse;
import com.stayview.backend.domain.chat.service.ChatService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

	private final ChatService chatService;

	public ChatController(ChatService chatService) {
		this.chatService = chatService;
	}

	/**
	 * 내가 참여 중인 채팅방 목록을 조회한다.
	 */
	@GetMapping
	public ApiResponse<List<ChatRoomSummaryResponse>> listMyRooms(@RequestHeader("X-User-Id") Long userId) {
		return ApiResponse.ok(chatService.listMyRooms(userId));
	}

	/**
	 * 특정 채팅방의 메시지 목록을 조회한다.
	 */
	@GetMapping("/{chatId}")
	public ApiResponse<ChatRoomDetailResponse> getRoom(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable("chatId") Long chatRoomId
	) {
		return ApiResponse.ok(chatService.getRoom(userId, chatRoomId));
	}

	/**
	 * 특정 채팅방에 메시지를 전송한다.
	 */
	@PostMapping("/{chatId}")
	public ApiResponse<ChatMessageResponse> sendMessage(
		@RequestHeader("X-User-Id") Long userId,
		@PathVariable("chatId") Long chatRoomId,
		@Valid @RequestBody ChatMessageRequest request
	) {
		return ApiResponse.ok(chatService.sendMessage(userId, chatRoomId, request));
	}
}
