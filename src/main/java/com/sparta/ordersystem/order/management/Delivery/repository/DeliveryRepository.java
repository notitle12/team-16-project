package com.sparta.ordersystem.order.management.Delivery.repository;


import com.sparta.ordersystem.order.management.Delivery.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
}
