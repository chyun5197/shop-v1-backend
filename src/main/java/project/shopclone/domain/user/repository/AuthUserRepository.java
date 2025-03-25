package project.shopclone.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shopclone.domain.user.entity.AuthUser;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByEmail(String email);
    Optional<AuthUser> findByEmailAndChannel(String email, String channel);
    Optional<AuthUser> findByOauthId(String oauthId);
}
