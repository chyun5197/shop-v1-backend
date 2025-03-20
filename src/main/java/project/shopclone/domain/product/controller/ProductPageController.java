package project.shopclone.domain.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.shopclone.domain.product.service.ProductService;
import project.shopclone.domain.product.service.response.ProductPageResponse;

@RequiredArgsConstructor
@RestController
@Tag(name = "상품 조회 API")
@RequestMapping("/api/products")
public class ProductPageController {
    private final ProductService productService;

    @Operation(summary = "특정 브랜드의 상품 목록 페이지 조회")
    @GetMapping("/list")
    public ProductPageResponse readAllBrand(
//            @RequestParam("brand") String brand,
            @RequestParam("cate") Integer cateNo,
            @RequestParam("page") Long page,
            @RequestParam("pageSize") Long pageSize,
            @RequestParam("sorting") String sorting
//            @RequestParam("category") String category
    )  {
        return productService.readAllBrand(cateNo, page, pageSize, sorting);
    }

    // 전체 페이징 조회
    @Operation(summary = "전체 상품 목록 페이지 조회")
    @GetMapping("")
    public ProductPageResponse getAllProducts(
            @RequestParam("page") Long page,
            @RequestParam("pageSize") Long pageSize
    ) {
        return productService.getAllProducts(page, pageSize);
    }

    // 악기별 Like 페이징 조회  (Guitar/Bass/Acoustic)
//    @GetMapping("/inst")
//    public ProductPageResponse getAllInstProducts(
//            @RequestParam("cates") String cates,
//            @RequestParam("page") Long page,
//            @RequestParam("pageSize") Long pageSize
//    ){
//        return productService.getAllInstProducts(cates, page, pageSize);
//    }

    // 악기별 특정 페이징 조회
    @Operation(summary = "특정 카테고리(기타/베이스/어쿠스틱) 상품 목록 페이지 조회")
    @GetMapping("/category")
    public ProductPageResponse getAllInstProducts(
            @RequestParam("inst") String inst,
            @RequestParam("page") Long page,
            @RequestParam("pageSize") Long pageSize
    ){
        return productService.getAllInstProducts(inst, page, pageSize);
    }
}
