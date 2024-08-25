package com.sparta.ordersystem.order.management.Order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID>, OrderRepsoitoryCustom {
}
