package project.shopclone.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.shopclone.domain.product.service.ProductService;
import project.shopclone.domain.product.service.response.ProductPageResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductPageController {
    private final ProductService productService;

    // 카테번호 페이징 조회
    @GetMapping("/list")
    public ProductPageResponse readAllBrand(
//            @RequestParam("brand") String brand,
            @RequestParam("cate") Integer cateNo,
            @RequestParam("page") Long page,
            @RequestParam("pageSize") Long pageSize,
            @RequestParam("sorting") String sorting
//            @RequestParam("category") String category
    ){
        return productService.readAllBrand(cateNo, page, pageSize, sorting);
    }

    // 전체 페이징 조회
    @GetMapping("")
    public ProductPageResponse getAllProducts(
            @RequestParam("page") Long page,
            @RequestParam("pageSize") Long pageSize
    ) {
        return productService.getAllProducts(page, pageSize);
    }

    // 악기별 페이징 조회  (Guitar/Bass/Acoustic)
    @GetMapping("/inst")
    public ProductPageResponse getAllInstProducts(
            @RequestParam("cates") String cates,
            @RequestParam("page") Long page,
            @RequestParam("pageSize") Long pageSize
    ){
        return productService.getAllInstProducts(cates, page, pageSize);
    }

}
