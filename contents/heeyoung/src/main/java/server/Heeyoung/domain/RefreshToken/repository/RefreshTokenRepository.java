package server.Heeyoung.domain.RefreshToken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.Heeyoung.domain.RefreshToken.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByRefreshToken(String refreshToken);
    RefreshToken findByRefreshToken(String refreshToken);
    boolean existsByUserLoginId(String loginId);
}
