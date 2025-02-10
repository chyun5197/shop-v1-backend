package project.shopclone.domain.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shopclone.domain.product.entity.Brand;

import java.util.List;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByCateNo(Integer cateNo);
}
