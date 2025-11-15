package server.dongmin.domain.basket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.dongmin.domain.basket.dto.req.BasketItemRequest;
import server.dongmin.domain.basket.dto.res.BasketItemResponse;
import server.dongmin.domain.basket.entity.Basket;
import server.dongmin.domain.basket.entity.BasketItem;
import server.dongmin.domain.basket.repository.BasketItemRepository;
import server.dongmin.domain.basket.repository.BasketRepository;
import server.dongmin.domain.menu.entity.Menu;
import server.dongmin.domain.menu.repository.MenuRepository;
import server.dongmin.domain.user.entity.User;
import server.dongmin.domain.user.entity.UserDetailsImpl;
import server.dongmin.global.exception.error.CustomErrorCode;
import server.dongmin.global.exception.error.RestApiException;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final BasketItemRepository basketItemRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public BasketItemResponse addBasketItem(UserDetailsImpl userDetails, BasketItemRequest request) {
        User user = userDetails.getUser();

        // 추가하려는 메뉴 정보 조회
        Menu menu = menuRepository.findById(request.menuId())
                .orElseThrow(() -> new RestApiException(CustomErrorCode.MENU_NOT_FOUND));

        // 사용자의 장바구니 조회 또는 생성
        Basket basket = basketRepository.findByUserId(user.getUserId())
                .orElseGet(() -> basketRepository.save(Basket.create(user.getUserId(), menu.getStoreId())));

        // 현재 장바구니의 가게와 추가하려는 메뉴의 가게가 다른지 확인
        if (!basket.getStoreId().equals(menu.getStoreId())) {
            throw new RestApiException(CustomErrorCode.DIFFERENT_STORE_IN_BASKET);
        }

        BasketItem basketItem = checkMenuInBasket(basket, menu, request);

        BasketItem savedItem = basketItemRepository.save(basketItem);
        return BasketItemResponse.from(savedItem);
    }

    @Transactional(readOnly = true)
    public Slice<BasketItemResponse> getBasketItems(UserDetailsImpl userDetails, Pageable pageable) {
        // 사용자의 장바구니를 조회하여, 있으면 아이템 목록을, 없으면 비어있는 Slice를 반환
        return basketRepository.findByUserId(userDetails.getUser().getUserId())
                .map(basket -> basketItemRepository.findByBasketId(basket.getBasketId(), pageable)
                        .map(BasketItemResponse::from))
                .orElse(new SliceImpl<>(Collections.emptyList(), pageable, false));
    }

    // 장바구니에 이미 해당 메뉴가 있는지 확인하고, 있으면 수량 추가, 없으면 새로 생성
    public BasketItem checkMenuInBasket(Basket basket, Menu menu, BasketItemRequest request) {
        return basketItemRepository.findByBasketIdAndMenuId(basket.getBasketId(), menu.getMenuId())
                .map(item -> {
                    item.addQuantity(request.quantity());
                    return item;
                })
                .orElseGet(() -> BasketItem.create(basket.getBasketId(), request, menu.getPrice()));
    }
}
