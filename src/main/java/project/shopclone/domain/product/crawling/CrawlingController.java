package project.shopclone.domain.product.crawling;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import project.shopclone.domain.product.entity.Brand;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.product.repository.BrandRepository;
import project.shopclone.domain.product.repository.ProductRepository;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CrawlingController {
    private final CrawlingService productService;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    // DB에 저장하는 처음 한번만 실행하고 끝이기에 코드 실행시간이 빠를필요 없음
    // => 실행 오래 걸리더라도 정확하게끔, 나중에 코드 확장하기 편하게 알아보기 쉽도록 정리

    // 모든 브랜드 모든 상품의 정보 저장 (상세 이미지 제외) | 완료: Guitar1,
    @GetMapping("/crawling")
    public String crawl() {
        List<Brand> brands = brandRepository.findAll();
        for (Brand brand : brands){
            if (brand.getId() < 94 ){
                continue;
            }
            if (brand.getId()>=106) break;
            productService.crawlingPages(brand.getCateNo(), brand.getBrand(), brand.getCategory());
        }
        return "성공";
    }

//    @GetMapping("/delete")
//    public void del(){
//        List<Product> products = productRepository.findAllByBrand("PRS");
//        productRepository.deleteAll(products);
//
//    }

    // 상품 상세 이미지 저장

    @GetMapping("/crawling/brand")
    public String crawlB() {
        productService.crawlingCateAndBrand();
        return "성공";
    }

//    @GetMapping("/crawling/images")
//    public String crawlC() {
//        productService.crawlingOne();
//        return "성공";
//    }

}
