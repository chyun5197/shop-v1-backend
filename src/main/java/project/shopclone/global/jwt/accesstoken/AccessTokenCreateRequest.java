package project.shopclone.global.jwt.accesstoken;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenCreateRequest {
    private String refreshToken;
}
