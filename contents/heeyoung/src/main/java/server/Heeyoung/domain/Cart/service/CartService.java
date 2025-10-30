package server.Heeyoung.domain.Cart.service;

import lombok.RequiredArgsConstructor;
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

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(()->new RestApiException(ErrorCode.USER_NOT_FOUND));

        // 해당 유저의 장바구니 조회
        Cart cart = cartRepository.findByUserIdWithMenus(userId)
                .orElseThrow(()->new RestApiException(ErrorCode.CART_NOT_FOUND));

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

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(()->new RestApiException(ErrorCode.USER_NOT_FOUND));

        // 장바구니 조회
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(()->new RestApiException(ErrorCode.CART_NOT_FOUND));

        if (cart != null) {
            cartMenuRepository.deleteAllByCart(cart);
            // User랑 Cart 연결 끊기
            user.clearCart();
            cartRepository.delete(cart);
        }
    }

}
