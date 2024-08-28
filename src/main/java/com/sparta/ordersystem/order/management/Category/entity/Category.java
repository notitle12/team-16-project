package com.sparta.ordersystem.order.management.Category.entity;


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
@Table(name="p_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Category {

    @Id
    @Column(name="category_id")
    private UUID categoryId;

    @Column(name="category_name")
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<Store> stores = new ArrayList<>();


    @Builder
    public Category(String categoryName) {
        this.categoryId = UUID.randomUUID();  // UUID 자동 생성
        this.categoryName = categoryName;
    }
}
