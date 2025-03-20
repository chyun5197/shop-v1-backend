package project.shopclone.domain.wish;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "위시리스트 API")
@RequestMapping("/api/wish")
public class WishController {
    private final WishService wishService;

    @Operation(summary="위시리스트 조회")
    @GetMapping("")
    public ResponseEntity<List<WishResponse>> findAll(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(wishService.findAll(token));
    }

    @Operation(summary="위시리스트에 상품 등록")
    @PostMapping("/{productId}")
    public ResponseEntity<Void> createWish(
            @RequestHeader("Authorization") String token,
            @PathVariable Long productId){
        return wishService.createWish(token, productId);
    }

    @Operation(summary="위시리스트에서 상품 삭제")
    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWish(
            @RequestHeader("Authorization") String token,
            @PathVariable Long wishId){
        return wishService.deleteWish(token, wishId);
    }

    @Operation(summary="체크한 상품들을 위시리스트에서 삭제", description = "위시리스트 화면에서 체크한 상품들을 위시리스트에서 삭제")
    @DeleteMapping("")
    public ResponseEntity<Void> deleteWishes(
            @RequestHeader("Authorization") String token,
            @RequestBody List<Long> productIds){
        if (productIds.isEmpty()) { // 체크 안하고 삭제 버튼 눌렀을때
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return wishService.deleteWishes(token, productIds);
    }

    @Operation(summary="위시리스트에 랜덤하게 추출한 상품 등록",
            description = "화면에서 체크한 상품들의 추가, 삭제 기능이 잘 작동하는지 확인하기 위해 편의상 만들어놓음")
    @PostMapping("/random")
    public ResponseEntity<Void> createRandomWish(
            @RequestHeader("Authorization") String token
    ){
        return wishService.createRandomWish(token);
    }
}
