//package project.shopclone.domain.cart;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import project.shopclone.domain.cart.entity.CartItem;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/cart")
//public class CartController {
//    private final CartService cartService;
//
//    @GetMapping("/{cartId}")
//    public ResponseEntity<CartResponse> findAll(@PathVariable Long id) {
//        return cartService.findAll(id);
//    }
//
//    @PostMapping("/{productId}")
//    public ResponseEntity<String> createCartItem(@PathVariable Long productId){
//        return cartService.createCartItem(productId);
//    }
//}
