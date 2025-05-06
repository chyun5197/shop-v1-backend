package project.shopclone.domain.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder
@Entity
public class ChatRoom {
    @Id
    @GeneratedValue
    private Long chatRoomId;

    @Column(unique = true, nullable = false)
    private Long customerId;
    private Long counselorId;
}
