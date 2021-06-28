package com.uuhnaut69.inventory.infrastructure.message.outbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class OutBox {

  @Id @GeneratedValue private UUID id;

  @Column(nullable = false)
  private String aggregateType;

  @Column(nullable = false)
  private UUID aggregateId;

  @Column(nullable = false)
  private String type;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private JsonNode payload;
}
