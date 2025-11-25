package server.Heeyoung.domain.CartMenu.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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
                .orElseGet(() -> {
                    log.info("기존 장바구니 없음 → 새 장바구니 생성: userId={}, storeId={}",
                            user.getId(), store.getId());
                    return cartRepository.save(Cart.builder()
                            .user(user)
                            .store(store)
                            .build());
                });
    }

    // 장바구니에 메뉴 추가
    @Transactional
    public CartMenuResponseDto addMenuToCart(CartMenuRequestDto dto, Long userId) {

        log.debug("장바구니 메뉴 추가 요청: userId={}, dto={}", userId, dto);

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("메뉴 추가 실패 - 유저 없음. userId={}", userId);
                    return new RestApiException(ErrorCode.USER_NOT_FOUND);
                });

        log.info("유저 조회 성공: userId={}, userName={}", userId, user.getName());

        // 가게 조회
        Store store = storeRepository.findById(dto.getStoreId())
                .orElseThrow(() -> {
                    log.warn("메뉴 추가 실패 - 가게 없음. storeId={}", dto.getStoreId());
                    return new RestApiException(ErrorCode.STORE_NOT_FOUND);
                });

        log.info("가게 조회 성공: storeId={}, storeName={}", store.getId(), store.getStoreName());

        // 장바구니 가져오기 (없으면 새로 생성)
        Cart cart = getOrCreateCart(user, store);

        log.debug("장바구니 확인 완료: cartId={}", cart.getId());

        // 메뉴 조회
        Menu menu = menuRepository.findById(dto.getMenuId())
                .orElseThrow(() -> {
                    log.warn("메뉴 추가 실패 - 메뉴 없음. menuId={}", dto.getMenuId());
                    return new RestApiException(ErrorCode.MENU_NOT_FOUND);
                });

        log.info("메뉴 조회 성공: menuId={}, menuName={}", menu.getId(), menu.getMenuName());

        // 장바구니 메뉴 객체 생성
        CartMenu cartMenu = CartMenu.builder()
                .cartMenuQuantity(dto.getCartMenuQuantity())
                .menu(menu)
                .build();

        // Cart의 addCartMenu 메서드로 추가
        cart.addCartMenu(cartMenu);

        // 저장
        cartMenuRepository.save(cartMenu);

        log.info("장바구니에 메뉴 추가 완료: cartMenuId={}, cartId={}, quantity={}",
                cartMenu.getId(), cart.getId(), dto.getCartMenuQuantity());

        return CartMenuResponseDto.from(cartMenu);
    }

    // 장바구니 메뉴 수량 수정
    @Transactional
    public CartMenuResponseDto updateCartMenuQuantity(Long userId, Long cartMenuId, Long newQuantity) {

        log.debug("장바구니 메뉴 수량 수정 요청: userId={}, cartMenuId={}, newQuantity={}",
                userId, cartMenuId, newQuantity);

        // 수량 검증
        if (newQuantity <= 0) {
            log.warn("수량 수정 실패 - 잘못된 수량: {}", newQuantity);
            throw new RestApiException(ErrorCode.INVALID_QUANTITY);
        }

        // 장바구니 메뉴 존재 확인
        CartMenu cartMenu = cartMenuRepository.findById(cartMenuId)
                .orElseThrow(() -> {
                    log.warn("수량 수정 실패 - CartMenu 없음. cartMenuId={}", cartMenuId);
                    return new RestApiException(ErrorCode.CART_MENU_NOT_FOUND);
                });

        // 본인 소유 검증
        Long ownerId = cartMenu.getCart().getUser().getId();
        if (!ownerId.equals(userId)) {
            log.warn("수량 수정 실패 - 권한 없음. 요청 userId={}, 실제 ownerId={}", userId, ownerId);
            throw new RestApiException(ErrorCode.UNAUTHORIZED_CART_ACCESS);
        }

        // 수량 업데이트
        cartMenu.updateQuantity(newQuantity);

        log.info("장바구니 메뉴 수량 수정 완료: cartMenuId={}, newQuantity={}", cartMenuId, newQuantity);

        return CartMenuResponseDto.from(cartMenu);
    }

    // 장바구니 메뉴 삭제
    @Transactional
    public void deleteCartMenu(Long userId, Long cartMenuId) {

        log.debug("장바구니 메뉴 삭제 요청: userId={}, cartMenuId={}", userId, cartMenuId);

        // 장바구니 메뉴 존재 확인
        CartMenu cartMenu = cartMenuRepository.findById(cartMenuId)
                .orElseThrow(() -> {
                    log.warn("메뉴 삭제 실패 - CartMenu 없음. cartMenuId={}", cartMenuId);
                    return new RestApiException(ErrorCode.CART_MENU_NOT_FOUND);
                });

        // 본인 소유 확인
        Long ownerId = cartMenu.getCart().getUser().getId();
        if (!ownerId.equals(userId)) {
            log.warn("메뉴 삭제 실패 - 권한 없음. 요청 userId={}, 실제 ownerId={}", userId, ownerId);
            throw new RestApiException(ErrorCode.UNAUTHORIZED_CART_ACCESS);
        }

        // 삭제
        cartMenuRepository.delete(cartMenu);

        log.info("장바구니 메뉴 삭제 완료: cartMenuId={}, userId={}", cartMenuId, userId);
    }
}
