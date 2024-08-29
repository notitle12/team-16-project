package com.sparta.ordersystem.order.management.Ai.repository;

import com.sparta.ordersystem.order.management.Ai.entity.Ai;
import org.hibernate.validator.constraints.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiRepository extends JpaRepository<Ai, UUID> {
}
