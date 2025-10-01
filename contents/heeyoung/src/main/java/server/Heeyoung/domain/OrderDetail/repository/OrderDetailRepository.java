package server.Heeyoung.domain.OrderDetail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.Heeyoung.domain.OrderDetail.entity.OrderDetail;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
