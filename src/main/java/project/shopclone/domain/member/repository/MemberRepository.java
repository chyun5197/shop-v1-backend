package project.shopclone.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shopclone.domain.member.entity.Member;
import project.shopclone.domain.member.entity.Role;
import project.shopclone.domain.user.entity.AuthUser;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByAuthUser(AuthUser authUser);
    List<Member> findAllByRole(Role role);
}
