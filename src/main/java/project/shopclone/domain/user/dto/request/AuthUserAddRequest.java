package project.shopclone.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class AuthUserAddRequest {
    private String email;
    private String password;
    private String name;
    private String address;
    private String phone;
}
