package project.shopclone.global.jwt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.shopclone.domain.user.entity.AuthUser;
import project.shopclone.domain.user.exception.AuthUserErrorCode;
import project.shopclone.domain.user.exception.AuthUserException;
import project.shopclone.domain.user.repository.AuthUserRepository;
import project.shopclone.global.jwt.accesstoken.AccessTokenCreateRequest;
import project.shopclone.global.jwt.accesstoken.AccessTokenCreateResponse;
import project.shopclone.global.jwt.service.TokenProvider;
import project.shopclone.global.jwt.service.TokenService;

@RequiredArgsConstructor
@RestController
@Tag(name = "토큰 API")
public class TokenController {
    private final TokenService tokenService;
    private final TokenProvider tokenProvider;
    private final AuthUserRepository authUserRepository;

    // 리프레시 토큰으로 새로운 액세스 토큰을 발급
    @Operation(summary = "액세스 토큰 재발급")
    @PostMapping("/api/token")
    public ResponseEntity<AccessTokenCreateResponse> createNewAccessToken(@RequestBody AccessTokenCreateRequest request){

        AuthUser authUser = authUserRepository.findById(tokenProvider.getAuthUserId(request.getRefreshToken()))
                .orElseThrow(() -> new AuthUserException(AuthUserErrorCode.USER_NOT_FOUND));
        String newAccessToken = tokenService.createNewAccessToken(authUser, request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AccessTokenCreateResponse(newAccessToken));
    }


}
