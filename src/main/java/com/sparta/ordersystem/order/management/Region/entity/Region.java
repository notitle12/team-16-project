package com.sparta.ordersystem.order.management.Region.entity;

import com.sparta.ordersystem.order.management.Store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="p_region")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Region {

    @Id
    @Column(name="region_id")
    private UUID regionId;

    @Column(name="region_name")
    private String regionName;

    @Column(name="is_active")
    @ColumnDefault("true")
    private boolean isActive;

    @OneToMany(mappedBy = "region")
    private List<Store> stores  = new ArrayList<>();

    @Builder
    public Region(String regionName) {
        this.regionId = UUID.randomUUID();  // UUID 자동 생성
        this.regionName = regionName;
        this.isActive = true;
    }

    public void updateRegionName(String regionName){
        this.regionName = regionName;
    }

    public void softDeleted(){
        this.isActive = false;
    }
}
