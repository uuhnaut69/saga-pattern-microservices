package com.peppa.delivery.api;

import com.peppa.delivery.domain.DeliveryRequest;
import com.peppa.delivery.domain.entity.Delivery;
import com.peppa.delivery.domain.port.DeliveryUseCasePort;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery")

public class DeliveryController {

    private final DeliveryUseCasePort deliveryUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Delivery createDelivery(@Valid @RequestBody DeliveryRequest deliveryRequest) {
        return deliveryUseCase.create(deliveryRequest);
    }

    @GetMapping("/{id}")
    public Delivery findById(@PathVariable UUID id) {
        return deliveryUseCase.findById(id);
    }
}
