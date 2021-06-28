package com.uuhnaut69.inventory.infrastructure.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "products")
public class ProductEntity {

  @Id private UUID id;

  @Column(nullable = false)
  private String name;

  private int stocks;
}
