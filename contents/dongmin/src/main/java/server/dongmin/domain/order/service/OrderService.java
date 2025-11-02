package server.dongmin.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import server.dongmin.domain.order.repository.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

}
