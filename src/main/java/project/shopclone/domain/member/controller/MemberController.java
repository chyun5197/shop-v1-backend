package project.shopclone.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.shopclone.domain.member.dto.MemberInfoResponse;
import project.shopclone.domain.member.dto.MemberUpdateRequest;
import project.shopclone.domain.member.service.MemberService;
import project.shopclone.domain.member.entity.Member;

@RequiredArgsConstructor
@RestController
@Tag(name = "마이페이지 API")
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "회원정보 조회")
    @GetMapping("")
    public ResponseEntity<MemberInfoResponse> getMemberInfo(@RequestHeader("Authorization") String token){
        Member member = memberService.getMember(token);
        return ResponseEntity.ok().body(MemberInfoResponse.from(member));
    }

    @Operation(summary = "회원정보 수정")
    @PutMapping("/update")
    public ResponseEntity<MemberInfoResponse> updateMemberInfo(@RequestHeader("Authorization") String token,
                                                               @RequestBody MemberUpdateRequest memberUpdateRequest
                                                               ){
        Member member = memberService.getMember(token);
        return memberService.updateMemberInfo(member, memberUpdateRequest);
    }

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/signout/{memberId}")
    public ResponseEntity<String> signOut(@RequestHeader("Authorization") String token,
                                          @PathVariable Long memberId){
        Member member = memberService.getMember(token);
        return ResponseEntity.ok(memberService.signOut(member, memberId));
    }
}
