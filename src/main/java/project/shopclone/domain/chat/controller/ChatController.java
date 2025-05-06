package project.shopclone.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;
import project.shopclone.domain.chat.dto.ChatHistoryResponse;
import project.shopclone.domain.chat.dto.ChatMessageDto;
import project.shopclone.domain.chat.dto.ChatRoomIdResponse;
import project.shopclone.domain.chat.dto.ChatRoomResponse;
import project.shopclone.domain.chat.entity.ChatRoom;
import project.shopclone.domain.chat.repository.ChatRoomRepository;
import project.shopclone.domain.chat.service.ChatService;
import project.shopclone.domain.member.entity.Member;
import project.shopclone.domain.member.entity.Role;
import project.shopclone.domain.member.service.MemberService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatService chatService;
    private final MemberService memberService;
    private final ChatRoomRepository chatRoomRepository;

    // 채팅방 정보 조회
    @GetMapping("")
    public ResponseEntity<ChatRoomResponse> getChatRoomInfo(@RequestHeader("Authorization") String token) {
        Member member = memberService.getMember(token);
        // 상담사일 경우 상담사 정보만 반환
        if (member.getRole().equals(Role.COUNSELOR)){{
            return ResponseEntity.ok(ChatRoomResponse.builder()
                            .chatRoomId(0L)
                            .memberId(member.getMemberId())
                            .role(member.getRole().name())
                    .build());
        }}
        // 고객일 경우 채팅방 반환 (채팅방 없으면 방번호 -1 반환)
//        ChatRoom chatRoom = chatRoomRepository.findByMemberId(member.getMemberId());
//        if (chatRoom == null) { // 채팅방 없으면 새로 생성
//            chatRoom = chatService.createChatRoom(member);
//        }
        return ResponseEntity.ok(chatService.getChatRoomInfo(member));
    }

    // 채팅방 생성
    @PostMapping("")
    public ResponseEntity<ChatRoomResponse> createChatRoom(@RequestHeader("Authorization") String token){
        Member member = memberService.getMember(token);
        return ResponseEntity.ok(chatService.createChatRoom(member));
    }

    // 채팅 내역 조회
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<List<ChatHistoryResponse>> getChatMessages(@PathVariable Long chatRoomId) {
//        ChatHistoryResponse test = new ChatHistoryResponse(1L, 5L, "상담사", "무엇을 도와드릴까요?");
        return ResponseEntity.ok().body(chatService.getChatHistory(chatRoomId));
    }

    // 상담사의 채팅방 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<ChatRoomIdResponse>> getChatRoomList(@RequestHeader("Authorization") String token) {
        Member member = memberService.getMember(token);
        return ResponseEntity.ok(chatService.getChatRoomListInfo(member));
    }

}
