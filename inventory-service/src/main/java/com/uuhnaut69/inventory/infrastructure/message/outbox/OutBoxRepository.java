package com.uuhnaut69.inventory.infrastructure.message.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OutBoxRepository extends JpaRepository<OutBox, UUID> {}
