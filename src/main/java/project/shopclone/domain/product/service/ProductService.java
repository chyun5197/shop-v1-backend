package project.shopclone.domain.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.product.repository.BrandRepository;
import project.shopclone.domain.product.service.response.*;
import project.shopclone.domain.product.repository.ProductRepository;
import project.shopclone.global.common.PageLimitCalculator;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    // 브랜드 정보 조회
    public BrandResponse getBrand(Integer cate) {
        return new BrandResponse(brandRepository.findByCateNo(cate));
    }

    // 상세 조회
    public ProductResponse getProduct(Long id) {
        return new ProductResponse(productRepository.findById(id).get());
    }

    // 브랜드별 리스트 조회
    public ProductPageResponse readAllBrand(String brand, Long page, Long pageSize, String sorting){
        List<ProductThumbResponse> productThumbResponseList;
        if(sorting.equals("new")){
            productThumbResponseList = productRepository.findAllBrand(brand, (page - 1) * pageSize, pageSize).stream()
                    .map(ProductThumbResponse::from)
                    .toList();
        }else if(sorting.equals("asc")){// 오름차순(낮은가격)
            productThumbResponseList = productRepository.findAllBrand(brand, (page - 1) * pageSize, pageSize, sorting).stream()
                    .map(ProductThumbResponse::from)
                    .toList();
        }else {
            productThumbResponseList = productRepository.findAllBrandDesc(brand, (page - 1) * pageSize, pageSize, sorting).stream()
                    .map(ProductThumbResponse::from)
                    .toList();
        }
        return ProductPageResponse.of(
                productThumbResponseList,
                productRepository.count(
                        brand,
                        PageLimitCalculator.calculatePageLimit(page, pageSize, 5L)
                ),
                productRepository.countByBrand(brand)
        );
    }

    // 임시
    public List<ProductResponse> getAllProduct() {
        List<Product> products = productRepository.getProductTmp();
        return products.stream()
                .map(ProductResponse::new)
                .toList();
    }


}
