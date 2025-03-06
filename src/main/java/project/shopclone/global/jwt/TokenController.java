package project.shopclone.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.shopclone.domain.user.entity.AuthUser;
import project.shopclone.domain.user.repository.AuthUserRepository;
import project.shopclone.global.jwt.accesstoken.AccessTokenCreateRequest;
import project.shopclone.global.jwt.accesstoken.AccessTokenCreateResponse;
import project.shopclone.global.jwt.service.TokenProvider;
import project.shopclone.global.jwt.service.TokenService;

@RequiredArgsConstructor
@RestController
public class TokenController {
    private final TokenService tokenService;
    private final TokenProvider tokenProvider;
    private final AuthUserRepository authUserRepository;

    // 리프레시 토큰으로 새로운 액세스 토큰을 발급
    @PostMapping("/api/token")
    public ResponseEntity<AccessTokenCreateResponse> createNewAccessToken(@RequestBody AccessTokenCreateRequest request){

//        AuthUser authUser = authUserRepository.findById(tokenProvider.getAuthUserId(request.getRefreshToken()))
//                .orElseThrow();

        AuthUser authUser = authUserRepository.findById(tokenService.getAuthUserIdRefreshToken(request.getRefreshToken()))
                .orElseThrow();
        String newAccessToken = tokenService.createNewAccessToken(authUser, request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AccessTokenCreateResponse(newAccessToken));
    }


}
