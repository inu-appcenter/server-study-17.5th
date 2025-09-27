package server.Heeyoung.domain.Store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.Heeyoung.domain.Store.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
