package server.Heeyoung.domain.User.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.Heeyoung.domain.User.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
