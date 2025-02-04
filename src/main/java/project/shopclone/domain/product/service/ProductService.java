package project.shopclone.domain.product.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.product.service.response.ProductPageResponse;
import project.shopclone.domain.product.service.response.ProductResponse;
import project.shopclone.domain.product.repository.ProductRepository;
import project.shopclone.domain.product.service.response.ProductResponseTmp;
import project.shopclone.domain.product.service.response.ProductThumbResponse;

import java.util.Collections;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findById(id).get();
//        Product product = productRepository.getProductById(id);
//        log.info(product.getName());
        return new ProductResponse(product);
    }

//    public ProductResponseTmp getProductTmp(Long id) {
//        Product product = productRepository.getProductById(id);
//        return new ProductResponseTmp(product);
//    }

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
                productRepository.countAll()
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
