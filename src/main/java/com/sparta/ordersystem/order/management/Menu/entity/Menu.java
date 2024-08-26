package com.sparta.ordersystem.order.management.Menu.entity;

import com.sparta.ordersystem.order.management.OrderMenu.OrderMenu;
import com.sparta.ordersystem.order.management.common.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_menu")
public class Menu extends Timestamped{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "menu_id")
    private UUID menuId;

    private UUID store_id;

    private String menu_name;

    private Integer cost;

    private String content;

    @Builder.Default
    @Column(name = "is_active")
    private boolean isActive = true;

    @OneToMany(mappedBy = "menu")
    private List<OrderMenu> orderMenuList = new ArrayList<>();

    public void deleteMenu(){
        this.isActive = false;
    }
}
