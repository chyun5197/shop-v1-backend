package project.shopclone.domain.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.shopclone.domain.product.dto.response.BrandResponse;
import project.shopclone.domain.product.dto.response.ProductPageResponse;
import project.shopclone.domain.product.dto.response.ProductResponse;
import project.shopclone.domain.product.service.ProductService;

@RequiredArgsConstructor
@RestController
@Tag(name = "상품 조회 API")
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "베스트 상품 목록 조회")
    @GetMapping("/best")
    public ProductPageResponse getBestProducts(){
        return productService.getBestProducts();
    }

    @Operation(summary = "검색 결과 상품 목록 조회")
    @GetMapping("/search")
    public ProductPageResponse search(@RequestParam("keyword") String keyword,
                                        @RequestParam(value = "cate", required = false) String category,
                                        @RequestParam(value = "brand", required = false) String brand,
                                        @RequestParam(value = "start", required = false) Integer start,
                                        @RequestParam(value = "end", required = false) Integer end,
                                        @RequestParam(value = "sort", required = false) String sorting,
                                        @RequestParam("page") Long page,
                                        @RequestParam("pageSize") Long pageSize
                                        ){
        // String 변수에서 디폴트 요청올때
        // 1. 포스트맨에서 파라미터 아예 안넣으면 brand = null -> isEmpty() 메서드 있으면 에러
        // 2. 포스트맨에서 변수명만 있고 값을 비z우면 brand = , isEmpty() = true
        // 3. 리액트에서 brand: null -> 스프링 brand = null, isEmpty() : false
        // 4. 리액트에서 brand: '' -> 스프링 brand = , isEmpty() : true (2번 4번 동일)
        // Integer에서는 어떻게 보내든 null로 도착, 리액트에서는 ''으로만 요청 허용
        System.out.println("category = " + category + ", isEmpty() = " + category.isEmpty() );
        System.out.println("brand = " + brand + ", isEmpty() = " + brand.isEmpty());
        System.out.println("start = " + start);
        System.out.println("end = " + end);
        System.out.println("sort = " + sorting);
        System.out.println("page = " + page);
        System.out.println("pageSize = " + pageSize);
        System.out.println("=====================");
        return productService.search(keyword, category, brand, sorting, start, end, page, pageSize);
    }

    @Operation(summary = "브랜드 정보 조회")
    @GetMapping("/brand/{cate}")
    public BrandResponse getBrand(@PathVariable Integer cate) {
        return productService.getBrand(cate);
    }


    @Operation(summary = "상품 상세 조회")
    @GetMapping("/detail/{id}")
    public ProductResponse getProduct(@PathVariable Long id) {return productService.getProduct(id);}

//    @Operation(summary = "상품 상세 이미지 목록 조회")
//    @GetMapping("/detail/{id}/images")
//    public List<String> getProductDetailImages(@PathVariable Long id) {
//        return productService.getProductDetailImages(id);
//    }

    // 임시
//    @GetMapping("/all")
//    public List<ProductResponse> getAllProduct() {
//        return productService.getAllProduct();
//    }

//    @GetMapping("/tmp/{id}")
//    public ProductResponseTmp getProductTmp(@PathVariable Long id) {
//        return productService.getProductTmp(id);
//    }


}
