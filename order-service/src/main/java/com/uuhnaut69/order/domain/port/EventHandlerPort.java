package com.uuhnaut69.order.domain.port;

import com.uuhnaut69.order.domain.entity.Order;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

public interface EventHandlerPort {

  void exportOutBoxEvent(Order order);

  Consumer<Message<String>> reserveCustomerBalanceStage();

  Consumer<Message<String>> reserveProductStockStage();
}
