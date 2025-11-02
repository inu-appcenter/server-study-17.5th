package server.dongmin.domain.menu.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import server.dongmin.domain.menu.entity.Menu;

import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu,Long> {

    Optional<Menu> findByMenuId(Long menuId);

    Slice<Menu> findByStoreId(Long storeId, Pageable pageable);

}
