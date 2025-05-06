package project.shopclone.domain.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.shopclone.domain.cart.service.CartService;
import project.shopclone.domain.cart.dto.response.CartResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "장바구니 API")
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    @Operation(summary="장바구니 목록 조회")
    @GetMapping("")
    public ResponseEntity<CartResponse> findAll(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(cartService.findAll(token));
    }

    @Operation(summary="장바구니에 아이템 등록")
    @PostMapping("/{productId}")
    public ResponseEntity<Void> createCartItem(
            @RequestHeader("Authorization") String token,
            @PathVariable Long productId,
            @RequestParam(value = "count", required = false) Integer count) {
//        System.out.println("개수 = " + count);
        count = count == null ? 1 : count;
        return cartService.createCartItem(token, productId, count);
    }

    @Operation(summary="체크한 아이템들을 장바구니에 등록", description = "위시리스트 화면에서 체크한 아이템들을 장바구니에 추가")
    @PostMapping("")
    public ResponseEntity<Void> createCartItemList(
            @RequestHeader("Authorization") String token,
            @RequestBody List<Long> productIds){
        if (productIds.isEmpty()) { // 체크 안하고 등록 버튼 눌렀을때
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return cartService.createCartItemList(token, productIds);
    }

    @Operation(summary="장바구니에서 아이템 삭제")
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(
                        @RequestHeader("Authorization") String token,
                        @PathVariable Long cartItemId) {
        return cartService.deleteCartItem(token, cartItemId);
    }

    @Operation(summary="체크한 아이템들을 장바구니에서 삭제", description = "장바구니 화면에서 체크한 아이템들을 장바구니에서 삭제")
    @DeleteMapping("")
    public ResponseEntity<Void> deleteCartItemList(
            @RequestHeader("Authorization") String token,
            @RequestBody List<Long> cartItemIds){
        if (cartItemIds.isEmpty()) { // 체크 안하고 삭제 버튼 눌렀을때
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return cartService.deleteCartItems(token, cartItemIds);
    }

    @Operation(summary="장바구니 비우기")
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(
            @RequestHeader("Authorization") String token
    ){
        return cartService.clearCarts(token);
    }

    @Operation(summary="장바구니 아이템의 수량 변경")
    @PatchMapping("/{cartItemId}")
    public ResponseEntity<Void> updateCartItem(
                        @PathVariable Long cartItemId,
                        @RequestBody Integer count){
        return cartService.updateCartItem(cartItemId, count);
    }
}
