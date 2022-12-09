package com.uuhnaut69.inventory.infrastructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.inventory.domain.entity.Product;
import com.uuhnaut69.inventory.domain.port.ProductRepositoryPort;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

  private final ObjectMapper mapper;

  private final ProductJpaRepository productJpaRepository;

  @Override
  public Optional<Product> findProductById(UUID productId) {
    var entity = productJpaRepository.findById(productId);
    return entity.map(productEntity -> mapper.convertValue(productEntity, Product.class));
  }

  @Override
  public Product saveProduct(Product product) {
    var entity = mapper.convertValue(product, ProductEntity.class);
    productJpaRepository.save(entity);
    return product;
  }
}
