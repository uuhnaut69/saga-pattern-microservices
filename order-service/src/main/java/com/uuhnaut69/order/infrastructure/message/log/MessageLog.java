package com.uuhnaut69.order.infrastructure.message.log;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.sql.Timestamp;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class MessageLog {

  @Id
  private UUID id;

  @Column(nullable = false)
  private Timestamp receivedAt;
}
