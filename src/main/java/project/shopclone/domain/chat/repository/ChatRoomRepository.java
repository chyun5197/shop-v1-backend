package project.shopclone.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shopclone.domain.chat.entity.ChatRoom;
import project.shopclone.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findByCustomerId(Long customerId);

}
