package project.shopclone.domain.wish.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shopclone.domain.member.entity.Member;
import project.shopclone.domain.member.service.MemberService;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.product.repository.ProductRepository;
import project.shopclone.domain.user.exception.AuthUserErrorCode;
import project.shopclone.domain.user.exception.AuthUserException;
import project.shopclone.domain.wish.dto.WishResponse;
import project.shopclone.domain.wish.entity.Wish;
import project.shopclone.domain.wish.exception.WishErrorCode;
import project.shopclone.domain.wish.exception.WishException;
import project.shopclone.domain.wish.repository.WishRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class WishService {
    private final WishRepository wishRepository;
    private final MemberService memberService;
    private final ProductRepository productRepository;

    // 위시리스트 조회
    public List<WishResponse> findAll(Member member) {
        return member.getWishList()
                .stream()
                .map(WishResponse::from)
                .toList();
    }

    // 위시 등록
    @Transactional
//    @Retryable(
//            retryFor = {ObjectOptimisticLockingFailureException.class},
//            maxAttempts = 4,
//            backoff = @Backoff(300) // 재시도 간격 300ms
//    )
    public void createWish(Member member, Long productId) {
        Product product = productRepository.findById(productId).get();
        // 이미 등록된 좋아요일 경우 (memberId, productId)
        if(wishRepository.findByMemberAndProduct(member, product).isPresent()) {
            throw new WishException(WishErrorCode.WISH_ALREADY_REPORTED);
        }
        wishRepository.save(Wish.builder()
                .member(member)
                .product(product)
                .build());

        member.plusWishCount(); // 멤버의 좋아요 개수++
//        product.plusWishCount(); // 상품의 좋아요 개수++
    }



    // 위시 삭제
    @Transactional
    public void deleteWish(Member member, Long wishId) {
        Wish wish = wishRepository.findById(wishId).get();

        // 위시 개수--
        member.minusWishCount();
//        wish.getProduct().minusWishCount();

        wishRepository.delete(wish);
    }

    // 위시 여러개 삭제
    @Transactional
    public ResponseEntity<Void> deleteWishes(Member member, List<Long> productIds) {
        // 관심상품 비우기
        if (productIds.get(0) == -1){
            List<Wish> wishList = member.getWishList();
            wishList.forEach(wish -> wish.getProduct().minusWishCount()); // 상품들 위시 개수--
            member.getWishList().clear();
        }

        // 선택한 관심상품 삭제
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
    public ResponseEntity<Void> createRandomWish(Member member) {
        // sql에서 기존 위시에 없는 위시로 랜덤하게 1개 뽑기
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

    // 상품의 위시 개수 증가
    @Transactional
    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 4,
            backoff = @Backoff(300) // 재시도 간격 300ms
    )
    public void addWishCount(Long productId) {
        Product product = productRepository.findById(productId).get();
        product.plusWishCount();
    }

    // 상품의 위시 개수 감소
    @Transactional
    @Retryable(
            retryFor = {ObjectOptimisticLockingFailureException.class},
            maxAttempts = 4,
            backoff = @Backoff(300)
    )
    public void subWishCount(Long productId) {
        Product product = productRepository.findById(productId).get();
        product.minusWishCount();
    }
}
