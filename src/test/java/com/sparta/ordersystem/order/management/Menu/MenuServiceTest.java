package com.sparta.ordersystem.order.management.Menu;

import com.sparta.ordersystem.order.management.Menu.dto.MenuResponseDto;
import com.sparta.ordersystem.order.management.Menu.dto.UpdateRequestDto;
import com.sparta.ordersystem.order.management.Menu.entity.Menu;
import com.sparta.ordersystem.order.management.Menu.repository.MenuRepository;
import com.sparta.ordersystem.order.management.Menu.service.MenuService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

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

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 삭제 시 존재하지 않는 메뉴 ID로 실패케이스")
    void testErrorDeleteMenuNotExistedMenuId(){
        //given
        UUID MenuId = UUID.randomUUID();
        String expectedMessage = "존재하지 않는 메뉴 ID";

        given(menuRepository.findByMenuIdAndAndIsActiveTrue(MenuId)).willReturn(Optional.empty());
        given(messageSource.getMessage("not.found.menu.id", new UUID[]{MenuId}, "존재하지 않는 메뉴 ID", Locale.getDefault()))
                .willReturn(expectedMessage);

        //when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.deleteMenu(MenuId));

        //then
        assertEquals(exception.getMessage(),expectedMessage);
    }

    @Test
    @DisplayName("메뉴 삭제 성공케이스")
    void testSuccessDeleteMenu(){

        UUID menuId = UUID.randomUUID();

        Menu menu = Menu.builder()
                .menuId(menuId)
                .isActive(true)
                .menu_name("test")
                .cost(10000)
                .content("test1")
                .build();

        given(menuRepository.findByMenuIdAndAndIsActiveTrue(menuId)).willReturn(Optional.of(menu));
        given(menuRepository.save(menu)).willReturn(menu);

        menuService.deleteMenu(menuId);

        verify(menuRepository, times(1)).save(menu);

    }

    @Test
    @DisplayName("메뉴 수정 - 존재하지 않는 메뉴 ID 실패케이스")
    void testErrorUpdateMenuNotExistedMenuId(){
        UUID MenuId = UUID.randomUUID();
        Menu menu = Menu.builder()
                .menuId(MenuId)
                .menu_name("test")
                .content("test")
                .cost(10000)
                .build();

        UpdateRequestDto dto = UpdateRequestDto.builder()
                .menu_name("updateTest")
                .content("updateTest")
                .cost(20000)
                .store_id(UUID.randomUUID())
                .build();

        String expectedMessage = "존재하지 않는 메뉴 ID";

        given(menuRepository.findByMenuIdAndAndIsActiveTrue(MenuId)).willReturn(Optional.empty());
        given(messageSource.getMessage("not.found.menu.id",new UUID[]{MenuId},"존재하지 않는 메뉴 ID",
                Locale.getDefault())).willReturn(expectedMessage);

        Exception exception = assertThrows(IllegalArgumentException.class,
        () -> menuService.updateMenu(dto,MenuId));

        assertEquals(exception.getMessage(),expectedMessage);
    }

    @Test
    @DisplayName("메뉴 수정 - 성공 케이스")
    void testSuccessUpdateMenu(){

        UUID menuId = UUID.randomUUID();
        Menu menu = Menu.builder()
                .menuId(menuId)
                .menu_name("test")
                .content("test")
                .cost(10000)
                .build();

        UpdateRequestDto dto = UpdateRequestDto.builder()
                .menu_name("updateTest")
                .content("updateTest")
                .cost(20000)
                .store_id(UUID.randomUUID())
                .build();

        given(menuRepository.findByMenuIdAndAndIsActiveTrue(menuId)).willReturn(Optional.of(menu));

        given(menuRepository.save(menu)).willReturn(menu);

        MenuResponseDto newMenu = menuService.updateMenu(dto,menuId);

        assertEquals(dto.getMenu_name(),newMenu.getMenu_name());
        assertEquals(menuId,newMenu.getMenu_id());

    }
}
