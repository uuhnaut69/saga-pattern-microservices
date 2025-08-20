package com.peppa.delivery.domain.port;

import com.peppa.delivery.domain.entity.Delivery;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepositoryPort {

    Optional<Delivery> findDeliveryById(UUID id);

    Delivery saveDelivery(Delivery delivery);


}
