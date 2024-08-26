package com.sparta.ordersystem.order.management.Order;

import com.sparta.ordersystem.order.management.Menu.Menu;
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
@Table(name = "p_order")
public class Order extends Timestamped {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator( // Hibernate를 사용하여 UUID를 자동으로 생성
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    UUID order_id;

    Long user_id;

    OrderType state;  //주문 상태

    @Column(nullable = false, columnDefinition = "deleteYn default 'true'")
    boolean is_active = true; //주문 취소여부

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

    public void updateState(OrderType newState)
    {
        this.state = newState;
    }

    public void deleteOrder(){
        this.is_active = false;
    }
}
