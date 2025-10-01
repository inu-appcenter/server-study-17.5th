package server.Heeyoung.domain.CartMenu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.Heeyoung.domain.CartMenu.entity.CartMenu;

public interface CartMenuRepository extends JpaRepository<CartMenu, Long> {
}
