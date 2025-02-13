package project.shopclone.domain.cart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.shopclone.domain.cart.service.CartService;
import project.shopclone.domain.cart.service.response.CartResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    // 장바구니 조회
    @GetMapping("")
    public ResponseEntity<CartResponse> findAll(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(cartService.findAll(token));
    }

    // 장바구니 상품 등록 (중복 허용X)
    @PostMapping("/{productId}")
    public ResponseEntity<Void> createCartItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long productId,
            @RequestParam(value = "count", required = false) Integer count) {
//        System.out.println("개수 = " + count);
        count = count == null ? 1 : count;
        return cartService.createCartItem(token, productId, count);
    }

    // 체크한 장바구니 상품들 등록
    @PostMapping("")
    public ResponseEntity<Void> createCartItemList(
            @RequestHeader("Authorization") String token,
            @RequestBody List<Long> productIds){
        if (productIds.isEmpty()) { // 체크 안하고 등록 버튼 눌렀을때
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return cartService.createCartItemList(token, productIds);
    }

    // 장바구니 아이템 삭제
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(
                        @RequestHeader("Authorization") String token,
                        @PathVariable Long cartItemId) {
        return cartService.deleteCartItem(token, cartItemId);
    }

    // 체크한 장바구니 아이템들 삭제
    @DeleteMapping("")
    public ResponseEntity<Void> deleteCartItemList(
            @RequestHeader("Authorization") String token,
            @RequestBody List<Long> cartItemIds){
        if (cartItemIds.isEmpty()) { // 체크 안하고 삭제 버튼 눌렀을때
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return cartService.deleteCartItems(token, cartItemIds);
    }

    // 장바구니 비우기
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(
            @RequestHeader("Authorization") String token
    ){
        return cartService.clearCarts(token);
    }


    // 장바구니 아이템 수량 변경
    @PatchMapping("/{cartItemId}")
    public ResponseEntity<Void> updateCartItem(
                        @PathVariable Long cartItemId,
                        @RequestBody Integer count){
        return cartService.updateCartItem(cartItemId, count);
    }

    // 장바구니 아이템(들)을 주문 목록으로 이동
}
