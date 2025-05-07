package project.shopclone.global.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    private final SimpMessagingTemplate messagingTemplate;

    public KafkaConsumerService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "chat-topic", groupId = "chat-group")
    public void consume(String message) {
        // Kafka에서 받은 메시지를 WebSocket 구독 경로로 전달
        messagingTemplate.convertAndSend("/sub/chat", message);
    }
}