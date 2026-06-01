package com.stayview.backend.chat.service;

import com.stayview.backend.common.error.BusinessException;
import com.stayview.backend.common.error.ErrorCode;
import com.stayview.backend.chat.dto.ChatMessageRequest;
import com.stayview.backend.chat.dto.ChatMessageResponse;
import com.stayview.backend.chat.dto.ChatRoomDetailResponse;
import com.stayview.backend.chat.dto.ChatRoomSummaryResponse;
import com.stayview.backend.chat.entity.ChatMessage;
import com.stayview.backend.chat.entity.ChatRoom;
import com.stayview.backend.chat.repository.ChatMessageRepository;
import com.stayview.backend.chat.repository.ChatRoomRepository;
import com.stayview.backend.user.entity.User;
import com.stayview.backend.user.service.UserService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ChatService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final UserService userService;

	public ChatService(
		ChatRoomRepository chatRoomRepository,
		ChatMessageRepository chatMessageRepository,
		UserService userService
	) {
		this.chatRoomRepository = chatRoomRepository;
		this.chatMessageRepository = chatMessageRepository;
		this.userService = userService;
	}

	public List<ChatRoomSummaryResponse> listMyRooms(Long userId) {
		userService.getRequiredUser(userId);
		return chatRoomRepository.findDistinctByTenant_UserIdOrSpace_Agent_UserIdOrderByCreatedAtDesc(userId, userId)
			.stream()
			.map(room -> ChatRoomSummaryResponse.from(room, getLastMessage(room.getChatRoomId())))
			.toList();
	}

	public ChatRoomDetailResponse getRoom(Long userId, Long chatRoomId) {
		ChatRoom room = getRequiredRoom(userId, chatRoomId);
		List<ChatMessageResponse> messages = chatMessageRepository.findByChatRoom_ChatRoomIdOrderBySentAtAsc(chatRoomId)
			.stream()
			.map(ChatMessageResponse::from)
			.toList();

		return new ChatRoomDetailResponse(ChatRoomSummaryResponse.from(room, getLastMessage(chatRoomId)), messages);
	}

	@Transactional
	public ChatMessageResponse sendMessage(Long userId, Long chatRoomId, ChatMessageRequest request) {
		User sender = userService.getRequiredUser(userId);
		ChatRoom room = getRequiredRoom(userId, chatRoomId);
		return ChatMessageResponse.from(chatMessageRepository.save(new ChatMessage(room, sender, request.message())));
	}

	private ChatRoom getRequiredRoom(Long userId, Long chatRoomId) {
		ChatRoom room = chatRoomRepository.findById(chatRoomId)
			.orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "채팅방을 찾을 수 없습니다."));
		if (!room.hasParticipant(userId)) {
			throw new BusinessException(ErrorCode.FORBIDDEN, "채팅방 참여자만 접근할 수 있습니다.");
		}
		return room;
	}

	private ChatMessage getLastMessage(Long chatRoomId) {
		return chatMessageRepository.findTopByChatRoom_ChatRoomIdOrderBySentAtDesc(chatRoomId)
			.orElse(null);
	}
}
