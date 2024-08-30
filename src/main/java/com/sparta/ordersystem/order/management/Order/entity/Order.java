package com.sparta.ordersystem.order.management.Order.entity;

import com.sparta.ordersystem.order.management.Menu.entity.Menu;
import com.sparta.ordersystem.order.management.OrderMenu.OrderMenu;
import com.sparta.ordersystem.order.management.Store.entity.Store;
import com.sparta.ordersystem.order.management.User.entity.User;
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
@Table(name = "p_order")
public class Order extends Timestamped {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator( // Hibernate를 사용하여 UUID를 자동으로 생성
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "order_id")
    UUID orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;  //주문 상태

    @Enumerated(EnumType.STRING)
    OrderType orderType;//

    //가게 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    Store store;

    @Column(nullable = false, name = "is_active")
    @Builder.Default
    boolean isActive = true; //주문 취소여부


    @OneToMany(mappedBy = "order", orphanRemoval = true, cascade = CascadeType.ALL)
    @Builder.Default
    List<OrderMenu> orderMenuList = new ArrayList<>();

    public void addMenu(Menu menu) {
        OrderMenu orderMenu = OrderMenu.builder()
                .order(this)
                .menu(menu)
                .build();
        orderMenuList.add(orderMenu);
    }

    public void updateState(OrderStatus newState)
    {
        this.orderStatus = newState;
    }

    public void deleteOrder(){
        this.isActive = false;
    }

    public boolean getIsActive(){
        return this.isActive;
    }
}
