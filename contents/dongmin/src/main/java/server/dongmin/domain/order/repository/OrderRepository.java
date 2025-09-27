package server.dongmin.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.dongmin.domain.order.entity.Order;
import server.dongmin.domain.order.entity.OrderStatus;
import server.dongmin.domain.store.entity.Store;
import server.dongmin.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {

    Optional<Order> findByOrderId(Long orderId);

    List<Order> findByUser(User user);

    // 유저의 주문 목록을 배송상태에 따라 List로 반환(생성일 기준 내림차순)
    List<Order> findByUserAndStatusOrderByCreatedAtDesc(User user, OrderStatus status);

    List<Order> findByStore(Store store);

}
