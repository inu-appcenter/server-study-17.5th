package server.Heeyoung.domain.CartMenu.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.Heeyoung.domain.Cart.entity.Cart;
import server.Heeyoung.domain.Cart.repository.CartRepository;
import server.Heeyoung.domain.CartMenu.dto.request.CartMenuRequestDto;
import server.Heeyoung.domain.CartMenu.dto.response.CartMenuResponseDto;
import server.Heeyoung.domain.CartMenu.entity.CartMenu;
import server.Heeyoung.domain.CartMenu.repository.CartMenuRepository;
import server.Heeyoung.domain.Menu.entity.Menu;
import server.Heeyoung.domain.Menu.repository.MenuRepository;
import server.Heeyoung.domain.Store.entity.Store;
import server.Heeyoung.domain.Store.repository.StoreRepository;
import server.Heeyoung.domain.User.entity.User;
import server.Heeyoung.domain.User.repository.UserRepository;
import server.Heeyoung.global.exception.ErrorCode;
import server.Heeyoung.global.exception.RestApiException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartMenuService {

    private final CartMenuRepository cartMenuRepository;
    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    // 장바구니에 메뉴 추가
    @Transactional
    public CartMenuResponseDto addMenuToCart(CartMenuRequestDto dto, Long userId, Long storeId) {

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(()->new RestApiException(ErrorCode.USER_NOT_FOUND));

        // 가게 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(()->new RestApiException(ErrorCode.STORE_NOT_FOUND));

        // 유저의 기존 장바구니 확인
        Optional<Cart> existingCart = cartRepository.findByUserId(userId);

        Cart cart;
        // 유저가 해당 가게에 대한 장바구니가 있다면
        if (existingCart.isPresent()) {
            Cart existing = existingCart.get();

            // 다른 가게의 장바구니인 경우
            if (!existing.getStore().getId().equals(storeId)) {
                throw new RestApiException(ErrorCode.DIFFERENT_STORE_CART_EXISTS);
            }

            cart = existing;
        } else {
            // 새 장바구니 생성
            cart = cartRepository.save(Cart.builder()
                    .user(user)
                    .store(store)
                    .build());
        }

        // 메뉴 조회
        Menu menu = menuRepository.findById(dto.getMenuId())
                .orElseThrow(()->new RestApiException(ErrorCode.MENU_NOT_FOUND));

        // 장바구니 메뉴 객체 생성
        CartMenu cartMenu = CartMenu.builder()
                .cartMenuQuantity(dto.getCartMenuQuantity())
                .menu(menu)
                .build();

        // Cart의 addMenu 메서드로 추가
        cart.addCartMenu(cartMenu);

        // 저장
        cartMenuRepository.save(cartMenu);

        return CartMenuResponseDto.from(cartMenu);
    }

    // 장바구니 메뉴 수량 수정
    @Transactional
    public CartMenuResponseDto updateCartMenuQuantity(Long userId, Long cartMenuId, Long newQuantity) {

        // 수량 검증
        if (newQuantity <= 0) {
            throw new RestApiException(ErrorCode.INVALID_QUANTITY);
        }

        // 장바구니 메뉴 존재 확인
        CartMenu cartMenu = cartMenuRepository.findById(cartMenuId)
                .orElseThrow(()->new RestApiException(ErrorCode.CART_MENU_NOT_FOUND));

        // 본인 소유 검증
        if (!cartMenu.getCart().getUser().getId().equals(userId)) {
            throw new RestApiException(ErrorCode.UNAUTHORIZED_CART_ACCESS);
        }

        // 수량 업데이트
        cartMenu.updateQuantity(newQuantity);

        return CartMenuResponseDto.from(cartMenu);
    }

    // 장바구니 메뉴 삭제
    @Transactional
    public void deleteCartMenu(Long userId, Long cartMenuId) {

        // 장바구니 메뉴 존재 확인
        CartMenu cartMenu = cartMenuRepository.findById(cartMenuId)
                .orElseThrow(() -> new RestApiException(ErrorCode.CART_MENU_NOT_FOUND));

        // 본인 소유 확인
        if (!cartMenu.getCart().getUser().getId().equals(userId)) {
            throw new RestApiException(ErrorCode.UNAUTHORIZED_CART_ACCESS);
        }

        // 삭제
        cartMenuRepository.delete(cartMenu);
    }
}
