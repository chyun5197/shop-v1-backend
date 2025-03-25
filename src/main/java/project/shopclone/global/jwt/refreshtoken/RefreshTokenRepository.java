package project.shopclone.global.jwt.refreshtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    Optional<RefreshToken> findByAuthUserId(Long authUserId);
}
