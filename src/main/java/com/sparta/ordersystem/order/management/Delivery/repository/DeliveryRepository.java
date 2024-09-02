package com.sparta.ordersystem.order.management.Delivery.repository;


import com.sparta.ordersystem.order.management.Delivery.entity.Delivery;
import com.sparta.ordersystem.order.management.Order.entity.Order;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {
    Optional<Delivery> findByDeliveryIdAndIsActiveTrue(UUID deliveryId);
    Optional<Delivery> findByOrderAndIsActiveTrue(Order order);
}
