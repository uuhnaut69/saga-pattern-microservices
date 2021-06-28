package com.uuhnaut69.order.infrastructure.repository;

import com.uuhnaut69.order.domain.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class OrderEntity {

  @Id private UUID id;

  @Column(nullable = false)
  private UUID customerId;

  @Column(nullable = false)
  private UUID productId;

  private BigDecimal price;

  private int quantity;

  private Timestamp createdAt;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private OrderStatus status;
}
