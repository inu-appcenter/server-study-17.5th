package server.dongmin.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.dongmin.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUserId(Long userId);

}
