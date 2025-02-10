//package project.shopclone.domain.cart;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import project.shopclone.domain.cart.entity.Cart;
//import project.shopclone.domain.product.repository.ProductRepository;
//
//@RequiredArgsConstructor
//@Service
//public class CartService {
//    private final CartRepository cartRepository;
//    private final ProductRepository productRepository;
//
//    public ResponseEntity<CartResponse> findAll(Long memberId) {
//        Cart cart = cartRepository.findByMemberId(memberId);
//        return null;
//    }
//
//    public ResponseEntity<String> createCartItem(Long productId) {
//        return null;
//    }
//}
