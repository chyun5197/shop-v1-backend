package project.shopclone.domain.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthUserAddRequest {
    private String email;
    private String password;
}
