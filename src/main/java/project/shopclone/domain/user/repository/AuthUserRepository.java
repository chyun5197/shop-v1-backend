package project.shopclone.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shopclone.domain.user.entity.AuthUser;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    AuthUser findByEmail(String email);
}
