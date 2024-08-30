package com.sparta.ordersystem.order.management.Order.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.ordersystem.order.management.Menu.entity.QMenu;
import com.sparta.ordersystem.order.management.Order.dto.OrderSearchDto;
import com.sparta.ordersystem.order.management.Order.entity.Order;
import com.sparta.ordersystem.order.management.Order.dto.OrderMenuDto;
import com.sparta.ordersystem.order.management.Order.dto.OrderResponseDto;
import com.sparta.ordersystem.order.management.Order.entity.QOrder;
import com.sparta.ordersystem.order.management.OrderMenu.QOrderMenu;
import com.sparta.ordersystem.order.management.User.entity.User;
import com.sparta.ordersystem.order.management.User.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepsoitoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<OrderResponseDto> searchOrders(OrderSearchDto searchDto,Pageable pageable, User user) {
        QOrder qOrder = QOrder.order;
        QMenu qMenu = QMenu.menu;
        QOrderMenu qOrderMenu = QOrderMenu.orderMenu;
        UserRoleEnum role = user.getRole();

        JPAQuery<Order> query = queryFactory.selectFrom(qOrder)
                .leftJoin(qOrder.orderMenuList,qOrderMenu).fetchJoin()
                .leftJoin(qOrderMenu.menu,qMenu).fetchJoin()
                .orderBy(
                        qOrder.created_at.desc(),
                        qOrder.updated_at.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        /*
        //고객인 경우 : 자신의 주문내역만 조회
        if(UserRoleEnum.CUSTOMER.equals(role)){
            query.where(qOrder.user.id.eq(user.getId()));
        }
        //사장님인 경우 : 가게 주문내역들 조회
        else if (UserRoleEnum.OWNER.equals(role))
        {
            //TODO : user 안에 store_id와 menu_id를 이용하여 가게들의 주문 내역 추출

        }else if(UserRoleEnum.MANAGER.equals(role) || UserRoleEnum.MASTER.equals(role))
        {//모든 권한을 가짐.

        }else {
            throw new AccessDeniedException("권한이 없습니다.");
        }

        */

        QueryResults<Order> results = query.fetchResults();

        List<OrderResponseDto> contents = results.getResults().stream()
                .map(this::convertOrderToOrderResponseDto).toList();

        long total = results.getTotal();

        return new PageImpl<>(contents,pageable,total);
    }

    @Override
    public OrderResponseDto getOrderById(UUID orderId,Long user_id) {
        QOrder qOrder = QOrder.order;
        QMenu qMenu = QMenu.menu;
        QOrderMenu qOrderMenu = QOrderMenu.orderMenu;

        Order order = queryFactory.selectFrom(qOrder)
                .leftJoin(qOrder.orderMenuList,qOrderMenu).fetchJoin()
                .leftJoin(qOrderMenu.menu,qMenu).fetchJoin()
                .where(qOrder.orderId.eq(orderId))
                .fetchOne();

        return convertOrderToOrderResponseDto(order);
    }

    private OrderResponseDto convertOrderToOrderResponseDto(Order order) {
        return OrderResponseDto.builder()
                .order_id(order.getOrderId())
                .user_id(order.getUser().getId())
                .state(order.getOrderStatus())
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
