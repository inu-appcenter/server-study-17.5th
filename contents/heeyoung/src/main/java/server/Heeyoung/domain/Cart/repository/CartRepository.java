package server.Heeyoung.domain.Cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.Heeyoung.domain.Cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
