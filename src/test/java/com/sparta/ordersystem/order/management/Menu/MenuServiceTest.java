package com.sparta.ordersystem.order.management.Menu;

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
}
