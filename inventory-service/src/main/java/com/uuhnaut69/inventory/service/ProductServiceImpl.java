package com.uuhnaut69.inventory.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uuhnaut69.common.PlacedOrderEvent;
import com.uuhnaut69.common.ReserveStockStatus;
import com.uuhnaut69.common.SagaStep;
import com.uuhnaut69.inventory.api.exception.NotFoundException;
import com.uuhnaut69.inventory.api.request.ProductRequest;
import com.uuhnaut69.inventory.domain.OutBox;
import com.uuhnaut69.inventory.domain.Product;
import com.uuhnaut69.inventory.repository.OutBoxRepository;
import com.uuhnaut69.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ObjectMapper mapper;

  private final OutBoxRepository outBoxRepository;

  private final ProductRepository productRepository;

  @Override
  public Product findById(UUID productId) {
    return productRepository.findById(productId).orElseThrow(NotFoundException::new);
  }

  @Override
  public Product create(ProductRequest productRequest) {
    var product = mapper.convertValue(productRequest, Product.class);
    return productRepository.save(product);
  }

  @Override
  public void reserveProduct(PlacedOrderEvent orderEvent) {
    var product = findById(orderEvent.productId());
    var outbox = new OutBox();
    outbox.setAggregateId(orderEvent.id());
    outbox.setAggregateType(SagaStep.RESERVE_PRODUCT_STOCK_RESPONSE.name());
    outbox.setPayload(mapper.convertValue(orderEvent, JsonNode.class));

    if (product.getStocks() - orderEvent.quantity() < 0) {
      outbox.setType(ReserveStockStatus.FAILED.name());
    } else {
      product.setStocks(product.getStocks() - orderEvent.quantity());
      productRepository.save(product);
      outbox.setType(ReserveStockStatus.SUCCESSFUL.name());
    }
    outBoxRepository.save(outbox);
  }

  @Override
  public void compensateStock(PlacedOrderEvent orderEvent) {
    var product = findById(orderEvent.productId());
    product.setStocks(product.getStocks() + orderEvent.quantity());
    productRepository.save(product);
  }
}
