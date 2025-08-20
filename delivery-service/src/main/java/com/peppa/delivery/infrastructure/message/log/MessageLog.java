package com.peppa.delivery.infrastructure.message.log;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageLog {

    @Id
    private UUID id;

    @Column(nullable = false)
    private Timestamp receivedAt;
}
