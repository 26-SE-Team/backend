package com.stayview.backend.domain.chat.repository;

import com.stayview.backend.domain.chat.entity.ChatMessage;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	List<ChatMessage> findByChatRoom_ChatRoomIdOrderBySentAtAsc(Long chatRoomId);

	Optional<ChatMessage> findTopByChatRoom_ChatRoomIdOrderBySentAtDesc(Long chatRoomId);
}
