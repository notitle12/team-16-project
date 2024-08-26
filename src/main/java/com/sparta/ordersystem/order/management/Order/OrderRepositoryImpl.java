package com.sparta.ordersystem.order.management.Order;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.ordersystem.order.management.Menu.QMenu;
import com.sparta.ordersystem.order.management.Order.dto.OrderMenuDto;
import com.sparta.ordersystem.order.management.Order.dto.OrderResponseDto;
import com.sparta.ordersystem.order.management.OrderMenu.QOrderMenu;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
                        .order_id(order.getOrder_id())
                        .user_id(order.user_id)
                        .state(order.getState())
                        .created_at(order.getCreated_at())
                        .created_by(order.getCreated_by())
                        .updated_at(order.getUpdated_at())
                        .updated_by(order.getUpdated_by())
                        .deleted_at(order.getDeleted_at())
                        .deleted_by(order.getDeleted_by())
                        .order_menu(order.getOrderMenuList().stream().map(
                                orderMenu -> new OrderMenuDto(
                                        orderMenu.getMenu().getMenu_id(),
                                        orderMenu.getMenu().getMenu_name()
                                )
                        ).toList())
                        .build()
        ).toList();

        long total = results.getTotal();

        return new PageImpl<>(contents,pageable,total);
    }

}
