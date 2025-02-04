package project.shopclone.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name="member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;
    private String address;
    private String phone;

    private Integer interestCount;
    private Integer cartCount;
}
