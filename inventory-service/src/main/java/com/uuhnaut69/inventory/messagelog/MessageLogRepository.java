package com.uuhnaut69.inventory.messagelog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageLogRepository extends JpaRepository<MessageLog, UUID> {}
