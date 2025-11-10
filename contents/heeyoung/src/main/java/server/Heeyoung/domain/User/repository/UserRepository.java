package server.Heeyoung.domain.User.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.Heeyoung.domain.User.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByLoginId(String loginId);
    Optional<User> findByLoginId(String loginId);
    boolean existsByEmail(String email);
}
