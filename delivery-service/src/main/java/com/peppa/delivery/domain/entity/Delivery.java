package com.peppa.delivery.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    private UUID id;

    private String address;

    private String status;

}
