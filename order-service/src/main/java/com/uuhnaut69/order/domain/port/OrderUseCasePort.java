package com.uuhnaut69.order.domain.port;

import com.uuhnaut69.order.domain.OrderRequest;
import java.util.UUID;

public interface OrderUseCasePort {

  void placeOrder(OrderRequest orderRequest);

  void updateOrderStatus(UUID orderId, boolean success);
}
