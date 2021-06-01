package com.uuhnaut69.inventory.messagelog;

import java.util.UUID;

public interface MessageLogService {

  boolean isProcessed(UUID messageId);

  void markAsProcessed(UUID messageId);
}
