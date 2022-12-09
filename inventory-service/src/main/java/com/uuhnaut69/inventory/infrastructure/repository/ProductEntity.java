package com.uuhnaut69.inventory.infrastructure.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "products")
public class ProductEntity {

  @Id
  private UUID id;

  @Column(nullable = false)
  private String name;

  private int stocks;
}
