package project.shopclone.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shopclone.domain.chat.document.ChatMessage;
import project.shopclone.domain.chat.dto.ChatMessageDto;
import project.shopclone.domain.chat.dto.ChatHistoryResponse;
import project.shopclone.domain.chat.dto.ChatRoomIdResponse;
import project.shopclone.domain.chat.dto.ChatRoomResponse;
import project.shopclone.domain.chat.entity.ChatRoom;
import project.shopclone.domain.chat.repository.ChatMessageRepository;
import project.shopclone.domain.chat.repository.ChatRoomRepository;
import project.shopclone.domain.member.entity.Member;
import project.shopclone.domain.member.entity.Role;
import project.shopclone.domain.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    // 채팅 내역 조회
    public List<ChatHistoryResponse> getChatHistory(Long chatroomId) {
//        List<ChatMessage> chatMessageHistory = chatMessageRepository.findAllByChatRoomIdOrderByTimestampAsc(chatroomId);
        List<ChatMessage> chatMessageHistory = chatMessageRepository.findAllByChatRoomId(chatroomId);
//        if (chatMessageHistory.isEmpty()) {
//            System.out.println("비어있음");
//        }
        return chatMessageHistory.stream().map(ChatHistoryResponse::from).toList();
    }

    // 메세지 저장
    public void saveMessage(ChatMessageDto chatMessageDto) {
//        System.out.println("메세지 저장할때 chatRoomId = " + chatMessageDto.getChatRoomId());
//        System.out.println("메세지 저장할때 role = " + chatMessageDto.getRole());
        chatMessageRepository.save(ChatMessage.builder()
                        .chatRoomId(chatMessageDto.getChatRoomId())
                        .memberId(chatMessageDto.getMemberId())
                        .role(chatMessageDto.getRole())
                        .message(chatMessageDto.getMessage())
                        .timestamp(LocalDateTime.now())
                .build());
    }


    // 고객이 입장할 채팅방 정보 반환
    public ChatRoomResponse getChatRoomInfo(Member member) {
//        Long memberId = member.getMemberId();
//        ChatRoom chatRoom = chatRoomRepository.findByMemberId(memberId);
        // 트랜잭션 자기호출 문제로 컨트롤러로 이동
//        if(chatRoom == null) { // 채팅방 없으면 새로 생성
//            chatRoom = createChatRoom(member);
//        }
        ChatRoom chatRoom = chatRoomRepository.findByCustomerId(member.getMemberId());
        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom == null ? -1L : chatRoom.getChatRoomId()) // 채팅방 없으면 방번호 -1
                .memberId(member.getMemberId())
                .role(member.getRole().name())
                .build();
    }

    // 채팅방 생성
    @Transactional
    public ChatRoomResponse createChatRoom(Member customer){
        // 상담사 중에서 한명 지정
        List<Member> counselorList = memberRepository.findAllByRole(Role.COUNSELOR);
        int rdIdx = ThreadLocalRandom.current().nextInt(counselorList.size());
        Member counselor = counselorList.get(rdIdx);
        // 채팅방 생성
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .customerId(customer.getMemberId())
                .counselorId(counselor.getMemberId())
                .build());
        // 상담사 첫 멘트 저장
        saveMessage(new ChatMessageDto(chatRoom.getChatRoomId(),
                counselor.getMemberId(),
                counselor.getRole().name(),
                "안녕하세요, 고객님.\n쇼핑몰 채팅상담센터 입니다.\n무엇을 도와드릴까요?"
        ));
        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .memberId(customer.getMemberId())
                .role(customer.getRole().name())
                .build();
    }

    // 채팅방 리스트 반환
    public List<ChatRoomIdResponse> getChatRoomListInfo(Member member) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();

        return chatRoomList.stream()
                .map(chatRoom -> new ChatRoomIdResponse(chatRoom.getChatRoomId(), chatRoom.getCustomerId()))
                .toList();
    }
}
