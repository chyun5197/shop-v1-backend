package project.shopclone.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.shopclone.domain.product.service.response.BrandResponse;
import project.shopclone.domain.product.service.response.ProductPageResponse;
import project.shopclone.domain.product.service.response.ProductResponse;
import project.shopclone.domain.product.service.ProductService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

//    @GetMapping("/test")
//    public String test() {
//        return "test 확인";
//    }

    //@GetMapping("/search")

    @GetMapping("/brand/{cate}")
    public BrandResponse getBrand(@PathVariable Integer cate) {
        return productService.getBrand(cate);
    }


    @GetMapping("/detail/{id}")
    public ProductResponse getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    // 임시
    @GetMapping("/all")
    public List<ProductResponse> getAllProduct() {
        return productService.getAllProduct();
    }

//    @GetMapping("/tmp/{id}")
//    public ProductResponseTmp getProductTmp(@PathVariable Long id) {
//        return productService.getProductTmp(id);
//    }

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
}
