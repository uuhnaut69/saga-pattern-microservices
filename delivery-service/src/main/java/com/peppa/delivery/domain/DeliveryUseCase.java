package com.peppa.delivery.domain;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.peppa.delivery.domain.entity.Delivery;
import com.peppa.delivery.domain.exception.NotFoundException;
import com.peppa.delivery.domain.port.DeliveryUseCasePort;
import com.peppa.delivery.infrastructure.repository.DeliveryRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryUseCase implements DeliveryUseCasePort {
    private final DeliveryRepositoryAdapter deliveryRepositoryAdapter;
    private final ObjectMapper mapper;

    @Override
    @Transactional
    public Delivery create(DeliveryRequest deliveryRequest) {
        Delivery delivery = mapper.convertValue(deliveryRequest, Delivery.class);
        delivery.setId(UUID.randomUUID());
        return deliveryRepositoryAdapter.saveDelivery(delivery);
    }

    @Override
    public Delivery findById(UUID id) {
        return deliveryRepositoryAdapter.findDeliveryById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    @Transactional
    public boolean createDelivery(PlacedDeliveryEvent placedDeliveryEvent) {
        Delivery delivery = mapper.convertValue(placedDeliveryEvent, Delivery.class);
        isAddressCorrect(delivery);
        deliveryRepositoryAdapter.saveDelivery(delivery);
        return true;
    }

    private void isAddressCorrect(Delivery delivery) {
        Objects.requireNonNull(delivery.getAddress(), "Wrong delivery address");
    }


}
