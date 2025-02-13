package project.shopclone.domain.user.service.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthUserAddRequest {
    private String email;
    private String password;
}
