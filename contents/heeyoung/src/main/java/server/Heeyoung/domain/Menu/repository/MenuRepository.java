package server.Heeyoung.domain.Menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.Heeyoung.domain.Menu.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
