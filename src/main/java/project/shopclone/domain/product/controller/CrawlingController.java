package project.shopclone.domain.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import project.shopclone.domain.product.service.Crawling;
import project.shopclone.domain.product.entity.Brand;
import project.shopclone.domain.product.repository.BrandRepository;
import project.shopclone.domain.product.repository.ProductRepository;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CrawlingController {
    @Value("${data.crawling.pw}")
    private String pw;

    private final Crawling crawling;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;

    // DB에 저장하는 처음 한번만 실행하고 끝이기에 코드 실행시간이 빠를필요 없음
    // => 실행 오래 걸리더라도 정확하게끔, 나중에 코드 확장하기 편하게 알아보기 쉽도록 정리

    // 모든 브랜드 모든 상품의 정보 저장 (상세 이미지 제외) | 완료: Guitar1,
    @GetMapping("/crawling")
    public String crawl() {
        List<Brand> brands = brandRepository.findAll();
        for (Brand brand : brands){
            if (brand.getId()<169){
                continue;
            }
            crawling.crawlingPages(brand.getCateNo(), brand.getBrand(), brand.getCategory());
            // 분류번호 작업
//            crawling.cateNumbering(brand.getCateNo(), brand.getBrand(), brand.getCategory());
        }
        return "성공";
    }

    @GetMapping("/numbering")
    public void numbering() {
        List<Brand> brands = brandRepository.findAll();
        for (Brand brand : brands){
            if (brand.getCategory().equals("Bass 2")) {
                break;
            }

            // 분류번호 작업
            crawling.cateNumbering(brand.getCateNo(), brand.getBrand(), brand.getCategory());
        }
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
        crawling.crawlingCateAndBrand();
        return "성공";
    }

//    @GetMapping("/crawling/images")
//    public String crawlC() {
//        crawling.crawlingOne();
//        return "성공";
//    }

}
