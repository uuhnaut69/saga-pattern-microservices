package com.uuhnaut69.order.messagelog;

import java.util.UUID;

public interface MessageLogService {

  boolean isProcessed(UUID messageId);

  void markAsProcessed(UUID messageId);
}
