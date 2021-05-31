package com.uuhnaut69.order.service;

import com.uuhnaut69.order.api.request.OrderRequest;

import java.util.UUID;

public interface OrderService {

  void placeOrder(OrderRequest orderRequest);

  void updateOrderStatus(UUID orderId, boolean success);
}
