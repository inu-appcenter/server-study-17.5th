package server.dongmin.domain.basket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.dongmin.domain.basket.entity.Basket;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket,Long>{

    Optional<Basket> findByUserId(Long userId);
}

