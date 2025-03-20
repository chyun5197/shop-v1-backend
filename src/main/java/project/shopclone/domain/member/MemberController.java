package project.shopclone.domain.member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
