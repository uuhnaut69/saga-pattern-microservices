package com.uuhnaut69.customer.domain.port;

import org.springframework.messaging.Message;

import java.util.function.Consumer;

public interface EventHandlerPort {

  Consumer<Message<String>> handleReserveCustomerBalanceRequest();

  Consumer<Message<String>> handleCompensateCustomerBalanceRequest();
}
