package project.shopclone.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.shopclone.domain.member.entity.Role;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private Long chatRoomId;
    private Long memberId;
    private String role;
    private String message;

}
