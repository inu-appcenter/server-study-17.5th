package server.dongmin.domain.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.dongmin.domain.menu.entity.Menu;
import server.dongmin.domain.store.entity.Store;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu,Long> {

    Optional<Menu> findByMenuId(Long menuId);

    List<Menu> findByStore(Store store);

}
