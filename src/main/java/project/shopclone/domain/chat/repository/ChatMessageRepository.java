package project.shopclone.domain.chat.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import project.shopclone.domain.chat.document.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
//    List<ChatMessage> findAllByChatRoomIdOrderByTimestampAsc(Long chatRoomId);
    List<ChatMessage> findAllByChatRoomId(Long chatRoomId);
}
