package com.uuhnaut69.order.messagelog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageLogServiceImpl implements MessageLogService {

  private final MessageLogRepository messageLogRepository;

  @Override
  @Transactional(readOnly = true)
  public boolean isProcessed(UUID messageId) {
    return messageLogRepository.findById(messageId).isPresent();
  }

  @Override
  public void markAsProcessed(UUID messageId) {
    var msg = new MessageLog(messageId, Timestamp.from(Instant.now()));
    messageLogRepository.save(msg);
  }
}
