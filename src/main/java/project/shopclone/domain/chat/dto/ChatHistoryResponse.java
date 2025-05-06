package project.shopclone.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.shopclone.domain.chat.document.ChatMessage;
import project.shopclone.domain.chat.entity.ChatRoom;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistoryResponse {
    private Long chatRoomId;
    private Long memberId;
    private String role;
    private String message;

    public static ChatHistoryResponse from(ChatMessage chatMessage) {
        ChatHistoryResponse chatHistoryResponse = new ChatHistoryResponse();
        chatHistoryResponse.chatRoomId = chatMessage.getChatRoomId();
        chatHistoryResponse.memberId = chatMessage.getMemberId();
        chatHistoryResponse.role = chatMessage.getRole();
        chatHistoryResponse.message = chatMessage.getMessage();
        return chatHistoryResponse;
    }

}
