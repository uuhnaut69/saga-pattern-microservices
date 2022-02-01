package com.uuhnaut69.order.infrastructure.message.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageLogRepository extends JpaRepository<MessageLog, UUID> {

  @Query(value = """
      SELECT EXISTS (SELECT 1 FROM message_log WHERE id = :messageId)
      """, nativeQuery = true)
  boolean isMessageProcessed(UUID messageId);
}
