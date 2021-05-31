package com.uuhnaut69.customer.repository;

import com.uuhnaut69.customer.domain.OutBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OutBoxRepository extends JpaRepository<OutBox, UUID> {}
