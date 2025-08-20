package com.peppa.delivery.infrastructure.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String status;
}
