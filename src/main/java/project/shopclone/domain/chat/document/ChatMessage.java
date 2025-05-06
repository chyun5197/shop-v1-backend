package project.shopclone.domain.chat.document;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "chat_messages")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ChatMessage {
    @Id
    private String id;

    private Long chatRoomId;
    private String role;                // 역할(상담사 or 고객)
    private Long memberId;              // 메시지 보낸 멤버 id
    private String message;             // 메시지 내용
    private LocalDateTime timestamp;    // 메시지 전송 시간
}
