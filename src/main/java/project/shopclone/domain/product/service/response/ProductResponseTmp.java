package project.shopclone.domain.product.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.shopclone.domain.product.entity.Product;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ProductResponseTmp {
    private Long id;
    private String name;            // 제목: 모델명 + 옵션 + (new/old) + 시리얼넘버  [세일 멘트 제외함]

    public ProductResponseTmp(Product product) {
        this.id = product.getId();
        this.name = product.getName();
    }
}
