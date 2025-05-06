package project.shopclone.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.shopclone.domain.chat.dto.ChatMessageDto;
import project.shopclone.domain.chat.service.ChatService;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    // 메세지 송신 및 수신, /pub가 생략된 모습. 클라이언트에선 /pub/message로 요청
    @MessageMapping("/message")
    public ResponseEntity<Void> receiveMessage(@RequestBody ChatMessageDto chatRequest) {
        // MongoDB에 메세지 저장
        chatService.saveMessage(chatRequest);

        // 메세지를 해당 채팅방 구독자들에게 전송
        messagingTemplate.convertAndSend("/sub/chatroom/"+chatRequest.getChatRoomId(), chatRequest);
        return ResponseEntity.ok().build();
    }
}
