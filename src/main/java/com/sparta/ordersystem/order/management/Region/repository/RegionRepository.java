package com.sparta.ordersystem.order.management.Region.repository;

import com.sparta.ordersystem.order.management.Region.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RegionRepository extends JpaRepository<Region, UUID> {
    boolean existsByRegionName(String regionName);
}
