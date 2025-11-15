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

    private Cart getOrCreateCart(User user, Store store) {
        return cartRepository.findByUserIdAndStoreId(user.getId(), store.getId())
                .orElseGet(()->cartRepository.save(Cart.builder()
                        .user(user)
                        .store(store)
                        .build()));
    }

    // 장바구니에 메뉴 추가
    @Transactional
    public CartMenuResponseDto addMenuToCart(CartMenuRequestDto dto, Long userId) {

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(ErrorCode.USER_NOT_FOUND));

        // 가게 조회
        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> new RestApiException(ErrorCode.STORE_NOT_FOUND));

        // 장바구니 가져오기 (없으면 새로 생성)
        Cart cart = getOrCreateCart(user, store);

        // 메뉴 조회
        Menu menu = menuRepository.findById(dto.getMenuId())
                .orElseThrow(() -> new RestApiException(ErrorCode.MENU_NOT_FOUND));

        // 장바구니 메뉴 객체 생성
        CartMenu cartMenu = CartMenu.builder()
                .cartMenuQuantity(dto.getCartMenuQuantity())
                .menu(menu)
                .build();

        // Cart의 addCartMenu 메서드로 추가
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
        if (!cartMenu.getCart().getUser().getId().equals(userId)) { // 이거 쿼리가 너무 많이 나감
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
