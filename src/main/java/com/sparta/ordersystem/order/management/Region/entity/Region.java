package com.sparta.ordersystem.order.management.Region.entity;

import com.sparta.ordersystem.order.management.Store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "region")
    private List<Store> stores  = new ArrayList<>();

    @Builder
    public Region(String regionName) {
        this.regionId = UUID.randomUUID();  // UUID 자동 생성
        this.regionName = regionName;
    }
}
