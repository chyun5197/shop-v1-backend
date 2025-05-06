package project.shopclone.domain.wish.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.shopclone.domain.member.entity.Member;
import project.shopclone.domain.member.service.MemberService;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.product.repository.ProductRepository;
import project.shopclone.domain.wish.dto.WishResponse;
import project.shopclone.domain.wish.entity.Wish;
import project.shopclone.domain.wish.repository.WishRepository;
import project.shopclone.domain.wish.service.WishService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "위시 API")
@RequestMapping("/api/wish")
public class WishController {
    private final WishService wishService;
    private final MemberService memberService;
    private final WishRepository wishRepository;

    @Operation(summary="위시리스트 조회")
    @GetMapping("")
    public ResponseEntity<List<WishResponse>> findAll(@RequestHeader("Authorization") String token) {
        Member member = memberService.getMember(token);
        return ResponseEntity.ok().body(wishService.findAll(member));
    }

    @Operation(summary="위시리스트에 상품 등록")
    @PostMapping("/{productId}")
    public ResponseEntity<Void> createWish(
            @RequestHeader("Authorization") String token,
            @PathVariable Long productId){
        Member member = memberService.getMember(token);
        wishService.createWish(member, productId);  // 위시 등록, 멤버의 위시리스트 개수++
        wishService.addWishCount(productId);        // 상품의 위시 개수++
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary="위시리스트에서 상품 삭제")
    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(
            @RequestHeader("Authorization") String token,
            @PathVariable Long wishId){
        Member member = memberService.getMember(token);
        Wish wish = wishRepository.findById(wishId).get();
        wishService.deleteWish(member, wishId); // 위시 삭제, 멤버의 위시리스트 개수--
        wishService.subWishCount(wish.getProduct().getId()); // 상품의 위시--
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary="체크한 상품들을 위시리스트에서 삭제", description = "위시리스트 화면에서 체크한 상품들을 위시리스트에서 삭제")
    @DeleteMapping("")
    public ResponseEntity<Void> deleteWishes(
            @RequestHeader("Authorization") String token,
            @RequestBody List<Long> productIds){
        if (productIds.isEmpty()) { // 체크 안하고 삭제 버튼 눌렀을때
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        Member member = memberService.getMember(token);
        return wishService.deleteWishes(member, productIds);
    }

    @Operation(summary="위시리스트에 랜덤하게 추출한 상품 등록",
            description = "화면에서 체크한 상품들의 추가, 삭제 기능이 잘 작동하는지 확인하기 위해 편의상 만들어놓음")
    @PostMapping("/random")
    public ResponseEntity<Void> createRandomWish(
            @RequestHeader("Authorization") String token
    ){
        Member member = memberService.getMember(token);
        return wishService.createRandomWish(member);
    }
}
