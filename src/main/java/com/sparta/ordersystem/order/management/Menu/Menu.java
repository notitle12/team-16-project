package com.sparta.ordersystem.order.management.Menu;

import com.sparta.ordersystem.order.management.OrderMenu.OrderMenu;
import com.sparta.ordersystem.order.management.common.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_menu")
public class Menu extends Timestamped{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID menu_id;

    private UUID store_id;

    private String menu_name;

    private Integer cost;

    private String description;

    private boolean isPublic;

    @OneToMany(mappedBy = "menu")
    private List<OrderMenu> orderMenuList = new ArrayList<>();
}
