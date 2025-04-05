package project.shopclone.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shopclone.domain.member.entity.Member;
import project.shopclone.domain.user.entity.AuthUser;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByAuthUser(AuthUser authUser);
}
