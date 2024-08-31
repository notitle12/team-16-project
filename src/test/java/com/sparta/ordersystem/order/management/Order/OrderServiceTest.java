package com.sparta.ordersystem.order.management.Order;

import com.sparta.ordersystem.order.management.Category.entity.Category;
import com.sparta.ordersystem.order.management.Menu.entity.Menu;
import com.sparta.ordersystem.order.management.Menu.exception.MenuNotFoundException;
import com.sparta.ordersystem.order.management.Menu.repository.MenuRepository;
import com.sparta.ordersystem.order.management.Order.dto.OrderMenuDto;
import com.sparta.ordersystem.order.management.Order.dto.OrderResponseDto;
import com.sparta.ordersystem.order.management.Order.dto.CreateOrderRequestDto;
import com.sparta.ordersystem.order.management.Order.entity.Order;
import com.sparta.ordersystem.order.management.Order.entity.OrderStatus;
import com.sparta.ordersystem.order.management.Order.entity.OrderType;
import com.sparta.ordersystem.order.management.Order.exception.OrderNotFoundException;
import com.sparta.ordersystem.order.management.Order.repository.OrderRepository;
import com.sparta.ordersystem.order.management.Order.service.OrderService;
import com.sparta.ordersystem.order.management.Region.entity.Region;
import com.sparta.ordersystem.order.management.Store.entity.Store;
import com.sparta.ordersystem.order.management.Store.repository.StoreRepository;
import com.sparta.ordersystem.order.management.User.entity.User;
import com.sparta.ordersystem.order.management.User.entity.UserRoleEnum;
import com.sparta.ordersystem.order.management.User.security.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private UserDetailsImpl userDetails;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    OrderService orderService;

    @Test
    @DisplayName("주문 등록 시 존재하지 않는 가게 ID - 실패케이스")
    void testErrorCreateOrderNotExistStoreId(){
        // given
        UUID menuId1 = UUID.randomUUID();
        UUID menuId2 = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();

        CreateOrderRequestDto requestDto = CreateOrderRequestDto.builder()
                .store_id(storeId)
                .menu_ids(Arrays.asList(menuId1, menuId2))
                .build();

        given(storeRepository.findById(requestDto.getStore_id())).willReturn(Optional.empty());

        // when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> orderService.createOrder(requestDto,userDetails.getUser())
        );

        // then
        assertEquals(exception.getMessage(),"Store with id " + storeId + " not found");
    }

    @Test
    @DisplayName("주문 등록 시 존재하지 않는 메뉴로 인한 에러")
    void testErrorCreateOrderNotExistMenuId(){
        // given
        UUID menuId1 = UUID.randomUUID();
        UUID menuId2 = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();

        CreateOrderRequestDto requestDto = CreateOrderRequestDto.builder()
                .store_id(storeId)
                .menu_ids(Arrays.asList(menuId1, menuId2))
                .build();

        Store store = new Store("testStore",new Category("test"),new Region("test"),new User());

        given(storeRepository.findById(requestDto.getStore_id())).willReturn(Optional.of(store));
        given(menuRepository.findByMenuIdAndIsActiveTrueAndStoreId(menuId1,store.getStoreId())).willReturn(Optional.empty());

        // when
        Exception exception = assertThrows(MenuNotFoundException.class,
                () -> orderService.createOrder(requestDto,userDetails.getUser())
        );

        // then
        assertEquals(exception.getMessage(),"Menu" + menuId1 + "does not exist");
    }

    @Test
    @DisplayName("대면 주문 시 사장님이 아닌 사용자가 주문을 시도할 때 에러 발생")
    void testErrorCreateOrderByNonOwnerUser() {
        // given
        UUID storeId = UUID.randomUUID();

        Store store = new Store("testStore",new Category("test"),new Region("test"),new User());

        User user = new User("testUser","!Passwrod123","test@test.com",UserRoleEnum.CUSTOMER);


        CreateOrderRequestDto requestDto = CreateOrderRequestDto.builder()
                .store_id(storeId)
                .orderType(OrderType.IN_PERSON)
                .build();

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

        // when
        Exception exception = assertThrows(AccessDeniedException.class, () -> {
            orderService.createOrder(requestDto, user);
        });

        // then
        assertEquals("대면 주문은 해당 가게의 사장님만 접수할 수 있습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("대면 주문 시 다른 가게의 사장님이 주문을 시도할 때 에러 발생")
    void testErrorCreateOrderByNonStoreOwnerUser() {
        // given
        UUID storeId = UUID.randomUUID();

        Store store = new Store("testStore",new Category("test"),new Region("test"),new User());

        User user = new User("testUser","!Passwrod123","test@test.com",UserRoleEnum.CUSTOMER);

        CreateOrderRequestDto requestDto = CreateOrderRequestDto.builder()
                .store_id(storeId)
                .orderType(OrderType.IN_PERSON)
                .build();

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));

        // when
        Exception exception = assertThrows(AccessDeniedException.class, () -> {
            orderService.createOrder(requestDto, user);
        });

        // then
        assertEquals("대면 주문은 해당 가게의 사장님만 접수할 수 있습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("주문 등록 성공 케이스")
    void testSuccessCreateOrder(){
        // given
        UUID menuId1 = UUID.randomUUID();
        UUID storeId = UUID.randomUUID();

        User user = new User("testUser","!Passwrod123","test@test.com",UserRoleEnum.CUSTOMER);

        CreateOrderRequestDto requestDto = CreateOrderRequestDto.builder()
                .store_id(storeId)
                .menu_ids(Arrays.asList(menuId1))
                .build();

        Store store = new Store("testStore",new Category("test"),new Region("test"),new User());
        Menu menu1 = Menu.builder()
                .menuId(menuId1)
                .build();


        given(storeRepository.findById(requestDto.getStore_id())).willReturn(Optional.of(store));
        given(menuRepository.findByMenuIdAndIsActiveTrueAndStoreId(requestDto.getMenu_ids().get(0), store.getStoreId())).willReturn(Optional.of(menu1));
        given(orderRepository.save(any(Order.class))).willReturn(new Order());

        orderService.createOrder(requestDto,user);
        // then
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("주문 상태 변경 API - 존재하지 않는 주문ID 실패케이스")
    void testErrorUpdateOrderStateNotExistOrderId()
    {
        UUID orderId = UUID.randomUUID();

        given(orderRepository.findByOrderIdAndIsActiveTrue(orderId)).willReturn(Optional.empty());

        Exception exception = assertThrows(OrderNotFoundException.class,
                () -> orderService.updateOrderState(OrderStatus.RUNNING, orderId,userDetails.getUser())
        );

        assertEquals("존재하지 않는 주문입니다.",exception.getMessage());
    }

    @Test
    @DisplayName("주문 상태 변경 API - 성공케이스")
    void testSuccessUpdateOrderState()
    {
        UUID orderId = UUID.randomUUID();
        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus(OrderStatus.CREATE)
                .build();

        given(orderRepository.findByOrderIdAndIsActiveTrue(order.getOrderId())).willReturn(Optional.of(order));

        given(orderRepository.save(order)).willReturn(order);

        Order newOrder = orderService.updateOrderState(OrderStatus.RUNNING, orderId,userDetails.getUser());

        assertEquals(order.getOrderId(), newOrder.getOrderId());
        assertEquals(newOrder.getOrderStatus(), OrderStatus.RUNNING);
    }

    @Test
    @DisplayName("주문 삭제 시 존재하지 않은 주문 ID로 실패케이스")
    void testErrorDeleteOrderIdNotExistOrderId(){
        UUID orderId = UUID.randomUUID();

        given(orderRepository.findByOrderIdAndIsActiveTrue(orderId)).willReturn(Optional.empty());

        Exception thrown = assertThrows(OrderNotFoundException.class,
            () -> orderService.deleteOrder(orderId)
        );

        assertEquals("존재하지 않는 주문 ID입니다.",thrown.getMessage());
    }

    @Test
    @DisplayName("주문 삭제 성공케이스")
    void testSuccessDeleteOrder(){
        UUID orderId = UUID.randomUUID();
        Order order = Order.builder()
                .orderId(orderId)
                .orderStatus(OrderStatus.CREATE)
                .isActive(false)
                .build();

        given(orderRepository.findByOrderIdAndIsActiveTrue(orderId)).willReturn(Optional.of(order));
        given(orderRepository.save(order)).willReturn(order);

        Order deleteOrder = orderService.deleteOrder(orderId);

        verify(orderRepository, times(1)).save(order);
        assertEquals(order.getOrderId(), deleteOrder.getOrderId());
        assertEquals(order.getOrderStatus(), OrderStatus.CREATE);
        assertEquals(order.getIsActive(), deleteOrder.getIsActive());
    }
}