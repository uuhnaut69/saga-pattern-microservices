package com.peppa.delivery.infrastructure.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peppa.delivery.domain.entity.Delivery;
import com.peppa.delivery.domain.port.DeliveryRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryAdapter implements DeliveryRepositoryPort {
    private final DeliveryJpaRepository deliveryJpaRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<Delivery> findDeliveryById(UUID id) {
        var entity = deliveryJpaRepository.findById(id);
        return entity.map( deliveryEntity -> objectMapper.convertValue(deliveryEntity, Delivery.class));
    }

    @Override
    public Delivery saveDelivery(Delivery delivery) {
        var entity = objectMapper.convertValue(delivery, DeliveryEntity.class);
        deliveryJpaRepository.save(entity);
        return delivery;
    }


}
