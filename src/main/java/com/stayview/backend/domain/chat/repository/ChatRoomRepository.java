package com.stayview.backend.chat.repository;

import com.stayview.backend.chat.entity.ChatRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	List<ChatRoom> findDistinctByTenant_UserIdOrSpace_Agent_UserIdOrderByCreatedAtDesc(Long tenantId, Long agentId);
}
