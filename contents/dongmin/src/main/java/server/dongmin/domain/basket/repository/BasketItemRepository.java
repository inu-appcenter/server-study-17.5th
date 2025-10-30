package server.dongmin.domain.basket.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import server.dongmin.domain.basket.entity.BasketItem;

import java.util.Optional;

public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {

    Slice<BasketItem> findByBasketId(Long basketId, Pageable pageable);

    Optional<BasketItem> findByBasketIdAndMenuId(Long basketId, Long menuId);
}
