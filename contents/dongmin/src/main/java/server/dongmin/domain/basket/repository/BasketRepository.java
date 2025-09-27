package server.dongmin.domain.basket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.dongmin.domain.basket.entity.Basket;
import server.dongmin.domain.order.entity.Order;
import server.dongmin.domain.store.entity.Store;
import server.dongmin.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket,Long>{

    Optional<Basket> findByBasketId(Long basketId);

    List<Basket> findByStoreAndOrderIsNotNull(Store store);

    // 사용자의 장바구니 상태인 품목들을 조회 (주문 전)
    List<Basket> findByUserAndOrderIsNull(User user);

    // 특정 주문에 포함된 품목들을 조회 (주문내역 조회 시)
    List<Basket> findByOrder(Order order);
}

