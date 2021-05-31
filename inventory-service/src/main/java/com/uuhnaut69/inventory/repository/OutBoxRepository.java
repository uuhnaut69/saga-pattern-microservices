package com.uuhnaut69.inventory.repository;

import com.uuhnaut69.inventory.domain.OutBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OutBoxRepository extends JpaRepository<OutBox, UUID> {}
