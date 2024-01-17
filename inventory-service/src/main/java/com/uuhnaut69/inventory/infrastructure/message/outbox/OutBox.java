package com.uuhnaut69.inventory.infrastructure.message.outbox;

import java.util.UUID;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.databind.JsonNode;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class OutBox {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false)
  private String aggregateType;

  @Column(nullable = false)
  private UUID aggregateId;

  @Column(nullable = false)
  private String type;

  @Type(JsonType.class)
  @Column(columnDefinition = "json")
  private JsonNode payload;
}
