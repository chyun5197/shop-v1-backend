package project.shopclone.domain.wish;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.shopclone.domain.wish.service.WishService;
import project.shopclone.domain.wish.service.response.WishResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/wish")
public class WishController {
    private final WishService wishService;

    // 위시리스트 조회
    @GetMapping("")
    public ResponseEntity<List<WishResponse>> findAll(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(wishService.findAll(token));
    }

    // 위시 등록 (중복 허용X)
    @PostMapping("/{productId}")
    public ResponseEntity<Void> createWish(
            @RequestHeader("Authorization") String token,
            @PathVariable Long productId){
        return wishService.createWish(token, productId);
    }

    // 위시 삭제 (요청: 위시ID)
    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(
            @RequestHeader("Authorization") String token,
            @PathVariable Long wishId){
        return wishService.deleteWish(token, wishId);
    }

    // 체크한 위시들 삭제(요청: 상품ID 리스트) or 관심상품 비우기(요청: [-1])
    @DeleteMapping("")
    public ResponseEntity<Void> deleteWishes(
            @RequestHeader("Authorization") String token,
            @RequestBody List<Long> productIds){
        if (productIds.isEmpty()) { // 체크 안하고 삭제 버튼 눌렀을때
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return wishService.deleteWishes(token, productIds);
    }
}
