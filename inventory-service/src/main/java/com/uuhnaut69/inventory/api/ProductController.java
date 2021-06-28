package com.uuhnaut69.inventory.api;

import com.uuhnaut69.inventory.domain.ProductRequest;
import com.uuhnaut69.inventory.domain.entity.Product;
import com.uuhnaut69.inventory.domain.port.ProductUseCasePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

  private final ProductUseCasePort productUseCase;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Product create(@RequestBody @Valid ProductRequest productRequest) {
    log.info("Create new product {}", productRequest);
    return productUseCase.create(productRequest);
  }

  @GetMapping("/{productId}")
  public Product findById(@PathVariable UUID productId) {
    return productUseCase.findById(productId);
  }
}
