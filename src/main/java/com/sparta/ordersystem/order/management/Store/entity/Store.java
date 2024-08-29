package com.sparta.ordersystem.order.management.Store.entity;

import com.sparta.ordersystem.order.management.Category.entity.Category;
import com.sparta.ordersystem.order.management.Region.entity.Region;
import com.sparta.ordersystem.order.management.User.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SoftDelete;

import java.util.UUID;

@Entity
@Table(name="p_store")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Store {

    @Id
    @Column(name="store_id")
    private UUID storeId;

    @Column(name="store_name", nullable = false)
    private String storeName;

    @Column(name="is_active")
    @ColumnDefault("true")
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @Builder
    public Store(String storeName, Category category, Region region, User user) {
        this.storeId = UUID.randomUUID();
        this.storeName = storeName;
        this.category = category;
        this.region = region;
        this.user = user;
        this.isActive = true;
    }

    public void updateStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public void updateRegion(Region region) {
        this.region = region;
    }
}