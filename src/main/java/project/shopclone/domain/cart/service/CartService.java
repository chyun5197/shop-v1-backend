package project.shopclone.domain.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shopclone.domain.cart.repository.CartItemRepository;
import project.shopclone.domain.cart.repository.CartRepository;
import project.shopclone.domain.cart.entity.Cart;
import project.shopclone.domain.cart.entity.CartItem;
import project.shopclone.domain.cart.service.response.CartItemResponse;
import project.shopclone.domain.cart.service.response.CartResponse;
import project.shopclone.domain.member.Member;
import project.shopclone.domain.member.MemberRepository;
import project.shopclone.domain.member.MemberService;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.product.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final MemberService memberService;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;

    // 장바구니 조회
    public CartResponse findAll(String token) {
        Member member = memberService.getMember(token);
        Cart cart = cartRepository.findByMember(member);
//        System.out.println("멤버명 = " + member.getEmail());

        // 카트 아이템 리스트 -> 카트 아이템 응답 리스트
        List<CartItemResponse> cartItemResponseList = cart.getCartItemList()
                .stream()
                .map(CartItemResponse::from)
                .toList();

        return CartResponse.builder()
                .cartCount(member.getCartCount())
                .savings(member.getSavings())
                .cartItemList(cartItemResponseList)
                .build();
    }

    // 장바구니 아이템 등록
    @Transactional
    public ResponseEntity<Void> createCartItem(String token, Long productId, Integer count) {
        Member member = memberService.getMember(token);
        Cart cart = cartRepository.findByMember(member);
        Product product = productRepository.findById(productId).get();

        // 중복일 경우
        if(cartItemRepository.findByProduct(product) != null) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null); // 208
        }

        member.plusCartCount(); // 카트 개수++
        cartItemRepository.save(CartItem.builder()
                .cart(cart)
                .count(count)
                .product(productRepository.findById(productId).orElseThrow())
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201
    }

    // 체크한 상품들 장바구니 아이템 등록
    @Transactional
    public ResponseEntity<Void> createCartItemList(String token, List<Long> productIds) {
        Member member = memberService.getMember(token);
        Cart cart = cartRepository.findByMember(member);

        for(Long productId : productIds) {
            Product product = productRepository.findById(productId).get();
            if(cartItemRepository.findByProduct(product) == null) { // 중복 아닐때 장바구니 추가
                member.plusCartCount(); // 카트 개수++
                cartItemRepository.save(CartItem.builder()
                        .cart(cart)
                        .count(1) // 수량 1개씩만
                        .product(productRepository.findById(productId).orElseThrow())
                        .build());
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 장바구니 아이템 삭제
    @Transactional
    public ResponseEntity<Void> deleteCartItem(String token, Long cartItemId) {
        Member member = memberService.getMember(token);
        Cart cart = cartRepository.findByMember(member);
        member.minusCartCount(); // 카트 개수--
        cart.getCartItemList()
                .remove(cartItemRepository.findById(cartItemId).orElseThrow());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 체크한 장바구니 아이템들 삭제
    @Transactional
    public ResponseEntity<Void> deleteCartItems(String token, List<Long> cartItemIds) {
//        cartItemRepository.deleteAllById(cartItemIds); // 이 코드는 왜 안되는지? 부모에서 삭제하는것만 허용인가
        Member member = memberService.getMember(token);
        Cart cart = cartRepository.findByMember(member);

        List<CartItem> cartItemList = new ArrayList<>();
        for(Long cartItemId : cartItemIds) {
            cartItemList.add(cartItemRepository.findById(cartItemId).orElseThrow());
        }
        cart.getCartItemList()
                .removeAll(cartItemList);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 장바구니 비우기
    @Transactional
    public ResponseEntity<Void> clearCarts(String token) {
        Member member = memberService.getMember(token);
        Cart cart = cartRepository.findByMember(member);
        cart.getCartItemList().clear();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 장바구니 아이템 수량 변경
    @Transactional
    public ResponseEntity<Void> updateCartItem(Long cartItemId, Integer count) {
        cartItemRepository.findById(cartItemId).orElseThrow()
                .updateCount(count);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
