package com.sparta.ordersystem.order.management.Menu.repository;

import com.sparta.ordersystem.order.management.Menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menu, UUID> {

    Optional<Menu> findByMenuIdAndAndIsActiveTrue(UUID id);
}
