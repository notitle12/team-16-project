package com.sparta.ordersystem.order.management.Payment.repository;

import com.sparta.ordersystem.order.management.Payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
