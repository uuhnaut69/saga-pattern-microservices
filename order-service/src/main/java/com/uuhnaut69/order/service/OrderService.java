package com.uuhnaut69.order.service;

import com.uuhnaut69.order.api.request.OrderRequest;

public interface OrderService {

    void placeOrder(OrderRequest orderRequest);
}
