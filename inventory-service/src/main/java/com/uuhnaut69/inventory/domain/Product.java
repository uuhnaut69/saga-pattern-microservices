package com.uuhnaut69.inventory.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "products")
public class Product {

  @Id @GeneratedValue private UUID id;

  @Column(nullable = false)
  private String name;

  private int stocks;
}
