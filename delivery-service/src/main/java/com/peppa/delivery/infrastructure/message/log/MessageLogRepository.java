package com.peppa.delivery.infrastructure.message.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageLogRepository extends JpaRepository<MessageLog, UUID> {

    boolean isMessageProcessed (UUID id);
}
