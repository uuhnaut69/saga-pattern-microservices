package com.peppa.delivery.domain.port;

import com.peppa.delivery.domain.DeliveryRequest;
import com.peppa.delivery.domain.PlacedDeliveryEvent;
import com.peppa.delivery.domain.entity.Delivery;
import java.util.UUID;

public interface DeliveryUseCasePort {
    Delivery create(DeliveryRequest deliveryRequest);

    Delivery findById(UUID id);

    boolean createDelivery(PlacedDeliveryEvent placedDeliveryEvent);
}
