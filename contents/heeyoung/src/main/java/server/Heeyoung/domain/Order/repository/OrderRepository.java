package server.Heeyoung.domain.Order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.Heeyoung.domain.Order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
