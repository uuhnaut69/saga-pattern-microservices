package com.uuhnaut69.order.domain.port;

import java.util.function.Consumer;
import org.springframework.messaging.Message;

public interface EventHandlerPort {

  Consumer<Message<String>> reserveCustomerBalanceStage();

  Consumer<Message<String>> reserveProductStockStage();
}
