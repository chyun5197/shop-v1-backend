package project.shopclone.domain.product.dto.response;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ProductPageResponse {
    private List<ProductThumbResponse> productThumbs;
    private Long productCount;
    private Integer totalCount;

    public static ProductPageResponse of(List<ProductThumbResponse> productThumbs, Long productCount, Integer totalCount
    ) {
        ProductPageResponse response = new ProductPageResponse();
        response.productThumbs = productThumbs;
        response.productCount = productCount;
        response.totalCount = totalCount;
        return response;
    }
}
