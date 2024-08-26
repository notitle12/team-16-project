package com.sparta.ordersystem.order.management.Order.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.ordersystem.order.management.Menu.entity.QMenu;
import com.sparta.ordersystem.order.management.Order.entity.Order;
import com.sparta.ordersystem.order.management.Order.dto.OrderMenuDto;
import com.sparta.ordersystem.order.management.Order.dto.OrderResponseDto;
import com.sparta.ordersystem.order.management.Order.entity.QOrder;
import com.sparta.ordersystem.order.management.OrderMenu.QOrderMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepsoitoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<OrderResponseDto> searchOrders(Pageable pageable) {
        QOrder qOrder = QOrder.order;
        QMenu qMenu = QMenu.menu;
        QOrderMenu qOrderMenu = QOrderMenu.orderMenu;

        QueryResults<Order> results = queryFactory.selectFrom(qOrder)
                .leftJoin(qOrder.orderMenuList,qOrderMenu).fetchJoin()
                .leftJoin(qOrderMenu.menu,qMenu).fetchJoin()
                .orderBy(
                        qOrder.created_at.desc(),
                        qOrder.updated_at.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<OrderResponseDto> contents = (List<OrderResponseDto>) results.getResults().stream().map(
                order -> OrderResponseDto.builder()
                        .order_id(order.getOrderId())
                        .user_id(order.getUser_id())
                        .state(order.getState())
                        .created_at(order.getCreated_at())
                        .created_by(order.getCreated_by())
                        .updated_at(order.getUpdated_at())
                        .updated_by(order.getUpdated_by())
                        .deleted_at(order.getDeleted_at())
                        .deleted_by(order.getDeleted_by())
                        .order_menu(order.getOrderMenuList().stream().map(
                                orderMenu -> OrderMenuDto.builder()
                                        .menu_id(orderMenu.getMenu().getMenuId())
                                        .menu_name(orderMenu.getMenu().getMenu_name())
                                        .cost(orderMenu.getMenu().getCost())
                                        .content(orderMenu.getMenu().getContent())
                                        .build()
                        ).toList())
                        .build()
        ).toList();

        long total = results.getTotal();

        return new PageImpl<>(contents,pageable,total);
    }

    @Override
    public OrderResponseDto getOrderById(UUID orderId) {
        QOrder qOrder = QOrder.order;
        QMenu qMenu = QMenu.menu;
        QOrderMenu qOrderMenu = QOrderMenu.orderMenu;

        Order order = queryFactory.selectFrom(qOrder)
                .leftJoin(qOrder.orderMenuList,qOrderMenu).fetchJoin()
                .leftJoin(qOrderMenu.menu,qMenu).fetchJoin()
                .where(qOrder.orderId.eq(orderId))
                .fetchOne();

        return OrderResponseDto.builder()
                .order_id(order.getOrderId())
                .user_id(order.getUser_id())
                .state(order.getState())
                .created_at(order.getCreated_at())
                .created_by(order.getCreated_by())
                .updated_at(order.getUpdated_at())
                .updated_by(order.getUpdated_by())
                .deleted_at(order.getDeleted_at())
                .deleted_by(order.getDeleted_by())
                .order_menu(
                        order.getOrderMenuList().stream().map(
                                orderMenu -> OrderMenuDto.builder()
                                        .menu_id(orderMenu.getMenu().getMenuId())
                                        .menu_name(orderMenu.getMenu().getMenu_name())
                                        .cost(orderMenu.getMenu().getCost())
                                        .content(orderMenu.getMenu().getContent())
                                        .build()
                        ).toList()
                )
                .build();
    }

}
