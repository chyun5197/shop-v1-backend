package project.shopclone.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthUserLoginResponse {
    private String email;
    private String refreshToken;
    private String accessToken;
}
