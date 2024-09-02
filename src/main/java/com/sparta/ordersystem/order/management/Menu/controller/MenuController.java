package com.sparta.ordersystem.order.management.Menu.controller;

import com.sparta.ordersystem.order.management.User.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.sparta.ordersystem.order.management.Menu.dto.CreateMenuRequestDto;
import com.sparta.ordersystem.order.management.Menu.dto.MenuResponseDto;
import com.sparta.ordersystem.order.management.Menu.dto.UpdateRequestDto;
import com.sparta.ordersystem.order.management.Menu.entity.Menu;
import com.sparta.ordersystem.order.management.Menu.service.MenuService;
import com.sparta.ordersystem.order.management.User.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<?> createMenu(@RequestBody CreateMenuRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails)
    {

        Menu createMenu = menuService.createMenu(requestDto, userDetails.getUser());

        if(createMenu != null){
            return ResponseEntity.ok().body("메뉴가 성공적으로 등록되었습니다.");
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("{menu_id}")
    public ResponseEntity<?> deleteMenu(@PathVariable UUID menu_id,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        menuService.deleteMenu(menu_id,userDetails.getUser());
        return ResponseEntity.ok().body("메뉴가 정상적으로 삭제되었습니다.");
    }

    @PutMapping(value = "{menu_id}")
    public ResponseEntity<?> updateMenu(@RequestBody UpdateRequestDto updateRequestDto, @PathVariable UUID menu_id
    , @AuthenticationPrincipal UserDetailsImpl userDetails)
    {
        MenuResponseDto dto = menuService.updateMenu(updateRequestDto, menu_id,userDetails.getUser());
        return ResponseEntity.ok().body("메뉴가 수정되었습니다.");
    }

    @GetMapping("/stores/{store_id}")
    public ResponseEntity<?> getAllMenusInStore(@PathVariable UUID store_id){
        return ResponseEntity.ok().body(menuService.getAllMenus(store_id));
    }
}
