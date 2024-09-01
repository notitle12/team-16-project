package com.sparta.ordersystem.order.management.Region.entity;


import com.sparta.ordersystem.order.management.Store.entity.Store;
import com.sparta.ordersystem.order.management.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="p_region")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Region extends Timestamped {

    @Id
    @Column(name="region_id")
    private UUID regionId;

    @Column(name="region_name")
    private String regionName;

    @ColumnDefault("true")
    @Column(name="is_active")
    boolean isActive;

    @OneToMany(mappedBy = "region")
    @ToString.Exclude
    private List<Store> stores = new ArrayList<>();


    @Builder
    public Region(String regionName) {
        this.regionId = UUID.randomUUID();  // UUID 자동 생성
        this.regionName = regionName;
        this.isActive = true;
    }

    public void updateRegionName(String regionName) {
        this.regionName = regionName;
    }

    public void softDeleted(Long userId){
        this.isActive = false;
        this.deleted_at = LocalDateTime.now();
        this.deleted_by = userId;

        if(!stores.isEmpty()) {
            for (Store store : stores) {
                store.softDeleted(userId);
            }
        }
    }
}
