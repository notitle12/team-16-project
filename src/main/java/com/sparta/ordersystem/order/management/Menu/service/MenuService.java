package com.sparta.ordersystem.order.management.Menu.service;

import com.sparta.ordersystem.order.management.Menu.dto.CreateMenuRequestDto;
import com.sparta.ordersystem.order.management.Menu.dto.MenuResponseDto;
import com.sparta.ordersystem.order.management.Menu.dto.UpdateRequestDto;
import com.sparta.ordersystem.order.management.Menu.entity.Menu;
import com.sparta.ordersystem.order.management.Menu.exception.MenuNotFoundException;
import com.sparta.ordersystem.order.management.Menu.repository.MenuRepository;
import com.sparta.ordersystem.order.management.Store.entity.Store;
import com.sparta.ordersystem.order.management.Store.repository.StoreRepository;
import com.sparta.ordersystem.order.management.User.entity.User;
import com.sparta.ordersystem.order.management.User.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MessageSource messageSource;
    private final StoreRepository storeRepository;

    /***
     * 메뉴 등록
     * 로그인한 사용자가 고객인 경우 등록 제한
     * 가게 사장님인 경우 자신의 가게에만 등록 가능
     * @param requestDto
     * @return
     */
    @Transactional
    public Menu createMenu(CreateMenuRequestDto requestDto, User user) {

        isCustomer(user.getRole());

        UUID storeId = requestDto.getStore_id();

        Store store = isExistedStore(storeId);

        isEqualsYourStore(user,store);

        Menu menu = CreateMenuRequestDto.toEntity(requestDto);

        menu.addStore(store);

        return menuRepository.save(menu);
    }

    /***
     * 등록된 메뉴를 숨김으로 상태 변경
     * @param menuId
     */
    @Transactional
    public void deleteMenu(UUID menuId, User user) {

        isCustomer(user.getRole());

        Menu menu = menuRepository.findByMenuIdAndIsActiveTrue(menuId).orElseThrow(
                () -> new MenuNotFoundException(messageSource.getMessage("not.found.menu.id",new UUID[]{menuId},"존재하지 않는 메뉴 ID",
                        Locale.getDefault()))
        );

        isEqualsYourStore(user,menu.getStore());

        menu.deleteMenu();

        menuRepository.save(menu);
    }

    /***
     * 메뉴의 정보를 수정(바꾸려고하는 전체의 정보를 담고있다)
     * @param updateRequestDto
     * @param menuId
     * @return
     */
    @Transactional
    public MenuResponseDto updateMenu(UpdateRequestDto updateRequestDto,UUID menuId,User user) {

        isCustomer(user.getRole());

        Store store = isExistedStore(updateRequestDto.getStore_id());

        isEqualsYourStore(user,store);

        Menu menu = menuRepository.findByMenuIdAndIsActiveTrue(menuId).orElseThrow(
                () -> new MenuNotFoundException(messageSource.getMessage("not.found.menu.id",new UUID[]{menuId},"존재하지 않는 메뉴 ID",
                        Locale.getDefault())
            )
        );

        menu.updateMenu(updateRequestDto);

        Menu newMenu = menuRepository.save(menu);

        return convertMenuToResponseDto(newMenu);
    }

    /***
     * 가게에 있는 모든 메뉴들을 조회
     * @param storeId
     * @return
     */
    public List<MenuResponseDto> getAllMenus(UUID storeId) {

        Store store = isExistedStore(storeId);

        List<Menu> menuList = menuRepository.findByStoreAndIsActiveTrue(store);

        return menuList.stream().map(this::convertMenuToResponseDto).toList();

    }

    /***
     * 가게의 존재여부 검증하는 로직
     * @param storeId
     * @return
     */
    private Store isExistedStore(UUID storeId) {

        return storeRepository.findById(storeId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 가게 ID입니다.")
        );
    }

    /***
     * 가게 사장님인 경우 자신의 가게의 주인인지 판별
     *
     */
    private void isEqualsYourStore(User user,Store store)
    {
        UserRoleEnum role = user.getRole();

        //가게 사장님 && 해당 가게의 사장님인 경우
        if(UserRoleEnum.OWNER.equals(role))
        {
            boolean result = user.getStores().stream().anyMatch(
                    storeItem -> storeItem.getStoreId().equals(store.getStoreId())
            );

            if(result == false)
            {
                throw new AccessDeniedException("자기 자신의 가게에만 접근 할수 있습니다.");
            }

        }else if(UserRoleEnum.MASTER.equals(role))
        {

        }else {
            throw new AccessDeniedException("해당 가게 사장님만 접근할 수 있습니다");
        }
    }
    /***
     * 로그인한 사용자가 고객인지 검증
     * @param role
     */
    private void isCustomer(UserRoleEnum role){
        if(UserRoleEnum.CUSTOMER.equals(role))
        {
            throw new AccessDeniedException("고객은 메뉴를 등록할 권한이 없습니다.");
        }
    }

    /***
     * Entity to ResponseDto 변환함수
     * @param menu
     * @return
     */
    private MenuResponseDto convertMenuToResponseDto(Menu menu) {
        return MenuResponseDto.builder()
                .menu_id(menu.getMenuId())
                .menu_name(menu.getMenu_name())
                .store_id(menu.getStore().getStoreId())
                .created_at(menu.getCreated_at())
                .updated_at(menu.getUpdated_at())
                .content(menu.getContent())
                .cost(menu.getCost())
                .is_active(menu.getIsActive())
                .build();
    }
}
