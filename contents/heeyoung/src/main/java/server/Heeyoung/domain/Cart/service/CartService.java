package server.Heeyoung.domain.Cart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.Heeyoung.domain.Cart.dto.response.CartMenuDto;
import server.Heeyoung.domain.Cart.dto.response.CartResponseDto;
import server.Heeyoung.domain.Cart.entity.Cart;
import server.Heeyoung.domain.Cart.repository.CartRepository;
import server.Heeyoung.domain.CartMenu.repository.CartMenuRepository;
import server.Heeyoung.domain.Store.repository.StoreRepository;
import server.Heeyoung.domain.User.entity.User;
import server.Heeyoung.domain.User.repository.UserRepository;
import server.Heeyoung.global.exception.ErrorCode;
import server.Heeyoung.global.exception.RestApiException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final CartRepository cartRepository;
    private final CartMenuRepository cartMenuRepository;

    // 장바구니 조회
    @Transactional(readOnly = true)
    public CartResponseDto findCart(Long userId) {

        log.debug("장바구니 조회 요청: userId={}", userId);

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(()-> {
                    log.warn("장바구니 조회 실패 - 유저 없음. userId={}", userId);
                    return new RestApiException(ErrorCode.USER_NOT_FOUND);
                });

        log.info("유저 조회 성공: userId={}, userName={}", userId, user.getName());

        // 해당 유저의 장바구니 조회
        Cart cart = cartRepository.findByUserIdWithMenus(userId)
                .orElseThrow(() -> {
                    log.warn("장바구니 조회 실패 - Cart 없음. userId={}", userId);
                    return new RestApiException(ErrorCode.CART_NOT_FOUND);
                });

        log.info("장바구니 조회 성공: cartId={}, menuCount={}", cart.getId(), cart.getCartMenuList().size());

         // cartMenuDto 리스트
        List<CartMenuDto> cartMenuDtoList =  cart.getCartMenuList().stream()
                .map(cm -> CartMenuDto.builder()
                        .menuName(cm.getMenu().getMenuName())
                        .quantity(cm.getCartMenuQuantity())
                        .build())
                .toList();

        return CartResponseDto.from(cart, cartMenuDtoList);
    }

    // 장바구니 삭제
    @Transactional
    public void deleteCart(Long userId) {

        log.debug("장바구니 삭제 요청: userId={}", userId);

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("장바구니 삭제 실패 - 유저 없음. userId={}", userId);
                    return new RestApiException(ErrorCode.USER_NOT_FOUND);
                });

        log.info("유저 조회 성공: userId={}, userName={}", userId, user.getName());

        // 장바구니 조회
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.warn("장바구니 삭제 실패 - Cart 없음. userId={}", userId);
                    return new RestApiException(ErrorCode.CART_NOT_FOUND);
                });

        log.info("장바구니 조회 성공: cartId={}", cart.getId());

        // 삭제 처리
        log.debug("장바구니 메뉴 전체 삭제 시작: cartId={}", cart.getId());
        cartMenuRepository.deleteAllByCart(cart);

        // User랑 Cart 연결 끊기
        log.debug("유저-카트 연결 해제: userId={}, cartId={}", userId, cart.getId());
        user.clearCart();

        // Cart 삭제
        cartRepository.delete(cart);
        log.info("장바구니 삭제 완료: userId={}, cartId={}", userId, cart.getId());

    }

}


