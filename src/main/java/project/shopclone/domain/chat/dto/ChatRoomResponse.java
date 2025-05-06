package project.shopclone.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomResponse {
    private Long chatRoomId;
    private Long memberId;  // 로그인해서 입장하려는 멤버의 ID
    private String role;    // 상담사, 고객
}
