package project.shopclone.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shopclone.domain.product.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
