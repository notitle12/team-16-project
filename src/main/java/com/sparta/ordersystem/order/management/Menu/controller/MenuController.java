package com.sparta.ordersystem.order.management.Menu.controller;

import com.sparta.ordersystem.order.management.Menu.dto.CreateMenuRequestDto;
import com.sparta.ordersystem.order.management.Menu.entity.Menu;
import com.sparta.ordersystem.order.management.Menu.service.MenuService;
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
    public ResponseEntity<?> createMenu(@RequestBody CreateMenuRequestDto requestDto)
    {
        Menu createMenu = menuService.createMenu(requestDto);
        if(createMenu != null){
            return ResponseEntity.ok().body("메뉴가 성공적으로 등록되었습니다.");
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("{menu_id}")
    public ResponseEntity<?> deleteMenu(@PathVariable UUID menu_id)
    {
        menuService.deleteMenu(menu_id);
        return ResponseEntity.ok().body("메뉴가 정상적으로 삭제되었습니다.");
    }
}
