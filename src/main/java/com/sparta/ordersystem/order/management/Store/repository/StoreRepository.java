package com.sparta.ordersystem.order.management.Store.repository;

import com.sparta.ordersystem.order.management.Store.entity.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {
    List<Store> findAllByIsActiveTrue(Pageable pageable);

    @Query("SELECT s FROM Store s WHERE s.user.user_id = :userId AND s.isActive = true")
    List<Store> findAllByUserIdAndIsActive(Long userId, Pageable pageable);

    List<Store> findAllByRegion_RegionIdAndIsActiveTrue(UUID regionId, Pageable pageable);
    List<Store> findAllByRegion_RegionIdAndCategory_CategoryIdAndIsActiveTrue(UUID regionId, UUID categoryId, Pageable pageable);
}
