package server.dongmin.domain.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.dongmin.domain.store.entity.Store;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store,Long>, StoreRepositoryCustom {

    Optional<Store> findByStoreId(Long StoreId);

}
