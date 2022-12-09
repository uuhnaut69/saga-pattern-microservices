package com.uuhnaut69.order.domain.port;

import com.uuhnaut69.order.domain.entity.Order;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {

  Optional<Order> findOrderById(UUID orderId);

  void saveOrder(Order order);

  void exportOutBoxEvent(Order order);
}
