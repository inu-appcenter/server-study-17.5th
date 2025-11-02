package server.dongmin.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.dongmin.domain.order.entity.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {

    Optional<Order> findByOrderId(Long orderId);

}
