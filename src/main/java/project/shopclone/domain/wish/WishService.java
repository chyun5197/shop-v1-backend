package project.shopclone.domain.wish;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shopclone.domain.member.Member;
import project.shopclone.domain.member.MemberService;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.product.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class WishService {
    private final WishRepository wishRepository;
    private final MemberService memberService;
    private final ProductRepository productRepository;

    // 위시리스트 조회
    public List<WishResponse> findAll(String token) {
        Member member = memberService.getMember(token);

        return member.getWishList()
                .stream()
                .map(WishResponse::from)
                .toList();
    }

    // 위시 등록
    @Transactional
    public ResponseEntity<Void> createWish(String token, Long productId) {
        Member member = memberService.getMember(token);
        Product product = productRepository.findById(productId).get();
        // 중복일 경우
        if(wishRepository.findByProduct(product) != null) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(null);
        }

        wishRepository.save(Wish.builder()
                .member(member)
                .product(product)
                .build());
        // 위시 개수++
        member.plusWishCount();
        product.plusWishCount();

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 위시 삭제
    @Transactional
    public ResponseEntity<Void> deleteWish(String token, Long wishId) {
        Member member = memberService.getMember(token);
        Wish wish = wishRepository.findById(wishId).get();

        // 위시 개수--
        member.minusWishCount();
        wish.getProduct().minusWishCount();

        wishRepository.delete(wish);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 위시 여러개 삭제
    @Transactional
    public ResponseEntity<Void> deleteWishes(String token, List<Long> productIds) {
        // 관심상품 비우기
        if (productIds.get(0) == -1){
            Member member = memberService.getMember(token);
            List<Wish> wishList = member.getWishList();
            wishList.forEach(wish -> wish.getProduct().minusWishCount()); // 상품들 위시 개수--
            member.getWishList().clear();
            return ResponseEntity.status(HttpStatus.OK).build();
        }

        // 선택한 관심상품 삭제
        Member member = memberService.getMember(token);
        List<Wish> wishList  = new ArrayList<>();
        for(Long productId : productIds) { // 상품ID->상품객체->위시객체
            Product product = productRepository.findById(productId).get();
            product.minusWishCount(); // 상품 위시 개수--
            wishList.add(wishRepository.findByProduct(product));
        }

        member.getWishList()
                .removeAll(wishList);
        member.updateWishCount(-productIds.size()); // 위시리시트 개수 감소
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 랜덤 위시 추가
    @Transactional
    public ResponseEntity<Void> createRandomWish(String token) {
        // sql에서 기존 위시에 없는 위시로 랜덤하게 1개 뽑기
        Member member = memberService.getMember(token);
        List<Product> randProducts = productRepository.findListOneNotInOriginWish(member.getMemberId());

        wishRepository.save(Wish.builder()
                .member(member)
                .product(randProducts.get(0))
                .build());
        // 위시 개수++
        member.plusWishCount();
        randProducts.get(0).plusWishCount();

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
