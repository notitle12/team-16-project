package com.sparta.ordersystem.order.management.Menu;

import com.sparta.ordersystem.order.management.Category.entity.Category;
import com.sparta.ordersystem.order.management.Menu.dto.MenuResponseDto;
import com.sparta.ordersystem.order.management.Menu.dto.UpdateRequestDto;
import com.sparta.ordersystem.order.management.Menu.entity.Menu;
import com.sparta.ordersystem.order.management.Menu.exception.MenuNotFoundException;
import com.sparta.ordersystem.order.management.Menu.repository.MenuRepository;
import com.sparta.ordersystem.order.management.Menu.service.MenuService;
import com.sparta.ordersystem.order.management.Region.entity.Region;
import com.sparta.ordersystem.order.management.Store.entity.Store;
import com.sparta.ordersystem.order.management.Store.repository.StoreRepository;
import com.sparta.ordersystem.order.management.User.entity.User;
import com.sparta.ordersystem.order.management.User.entity.UserRoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 삭제 시 존재하지 않는 메뉴 ID로 실패케이스")
    void testErrorDeleteMenuNotExistedMenuId(){
        //given
        UUID MenuId = UUID.randomUUID();
        String expectedMessage = "존재하지 않는 메뉴 ID";

        given(menuRepository.findByMenuIdAndIsActiveTrue(MenuId)).willReturn(Optional.empty());
        given(messageSource.getMessage("not.found.menu.id", new UUID[]{MenuId}, "존재하지 않는 메뉴 ID", Locale.getDefault()))
                .willReturn(expectedMessage);

        //when
        Exception exception = assertThrows(MenuNotFoundException.class,
                () -> menuService.deleteMenu(MenuId,new User()));

        //then
        assertEquals(exception.getMessage(),expectedMessage);
    }

    @Test
    @DisplayName("메뉴 삭제 성공케이스")
    void testSuccessDeleteMenu(){

        UUID menuId = UUID.randomUUID();

        User user = new User("test","test1233!","test@test.com",UserRoleEnum.OWNER);
        Store store = new Store("test",new Category("123"),new Region("123"),user);

        Menu menu = Menu.builder()
                .menuId(menuId)
                .isActive(true)
                .menu_name("test")
                .cost(10000)
                .store(store)
                .content("test1")
                .build();
        user.setStores(Collections.singletonList(store));

        given(menuRepository.findByMenuIdAndIsActiveTrue(menuId)).willReturn(Optional.of(menu));
        given(menuRepository.save(menu)).willReturn(menu);

        menuService.deleteMenu(menuId,user);
        verify(menuRepository, times(1)).save(menu);

    }

    @Test
    @DisplayName("메뉴 수정 - 접근권한이 없는 경우")
    void testErrorUpdateMenuNotExistedMenuId(){
        UUID MenuId = UUID.randomUUID();
        Menu menu = Menu.builder()
                .menuId(MenuId)
                .menu_name("test")
                .content("test")
                .cost(10000)
                .build();

        Store store = new Store("myStore",new Category("123"),new Region("123"),new User());
        String expectedMessage = "해당 가게 사장님만 접근할 수 있습니다";

        UpdateRequestDto dto = UpdateRequestDto.builder()
                .menu_name("updateTest")
                .content("updateTest")
                .cost(20000)
                .store_id(store.getStoreId())
                .build();

        given(storeRepository.findById(dto.getStore_id())).willReturn(Optional.of(store));

        Exception exception = assertThrows(AccessDeniedException.class,
        () -> menuService.updateMenu(dto,MenuId,new User()));

        assertEquals(exception.getMessage(),expectedMessage);
    }

    @Test
    @DisplayName("메뉴 수정 - 성공 케이스")
    void testSuccessUpdateMenu(){

        UUID menuId = UUID.randomUUID();

        User user = new User("test","test1233!","test@test.com",UserRoleEnum.OWNER);
        Store store = new Store("test",new Category("123"),new Region("123"),user);

        Menu menu = Menu.builder()
                .menuId(menuId)
                .isActive(true)
                .menu_name("test")
                .cost(10000)
                .store(store)
                .content("test1")
                .build();
        user.setStores(Collections.singletonList(store));

        UpdateRequestDto dto = UpdateRequestDto.builder()
                .menu_name("updateTest")
                .content("updateTest")
                .cost(20000)
                .store_id(store.getStoreId())
                .build();

        given(storeRepository.findById(dto.getStore_id())).willReturn(Optional.of(store));
        given(menuRepository.findByMenuIdAndIsActiveTrue(menuId)).willReturn(Optional.of(menu));
        given(menuRepository.save(menu)).willReturn(menu);

        MenuResponseDto newMenu = menuService.updateMenu(dto,menuId,user);
        assertEquals(dto.getMenu_name(),newMenu.getMenu_name());
        assertEquals(menuId,newMenu.getMenu_id());

    }
}
