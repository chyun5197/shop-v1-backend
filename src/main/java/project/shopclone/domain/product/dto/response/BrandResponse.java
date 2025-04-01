package project.shopclone.domain.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.shopclone.domain.product.entity.Brand;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class BrandResponse {
    private String brand;
    private String category;
    private String image;
    private Integer cateNo;

    public BrandResponse(Brand brand) {
        this.brand = brand.getBrand();
        this.category = brand.getCategory();
        this.image = brand.getImage();
        this.cateNo = brand.getCateNo();
    }
}
