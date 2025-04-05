package project.shopclone.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.shopclone.domain.member.dto.MemberInfoResponse;
import project.shopclone.domain.member.service.MemberService;
import project.shopclone.domain.member.entity.Member;

@RequiredArgsConstructor
@RestController
@Tag(name = "회원 API")
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "마이페이지 조회")
    @GetMapping("")
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@RequestHeader("Authorization") String token){
        Member member = memberService.getMember(token);
        return ResponseEntity.ok().body(MemberInfoResponse.from(member));
    }

//    @Operation(summary = "회원정보 수정")
//    @PutMapping("/update")
//    public ResponseEntity<MemberInfoResponse> updateMemberInfo(@RequestHeader("Authorization") String token,
//                                                               @RequestBody
//                                                               ){
//
//    }
}
