package project.shopclone.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.shopclone.domain.user.AuthUser;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByAuthUser(AuthUser authUser);
}
