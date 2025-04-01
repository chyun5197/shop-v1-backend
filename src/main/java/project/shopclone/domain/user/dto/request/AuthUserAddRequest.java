package project.shopclone.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthUserAddRequest {
    private String email;
    private String password;
    private String name;
    private String address;
    private String phone;
}
