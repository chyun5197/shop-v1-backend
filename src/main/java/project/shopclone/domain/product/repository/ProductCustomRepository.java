package project.shopclone.domain.product.repository;

import org.springframework.stereotype.Repository;
import project.shopclone.domain.product.entity.Product;

import java.util.List;

@Repository
public interface ProductCustomRepository {
    List<Product> searchAll(String keyword,
                            String category,
                            String brand,
                            String sorting,
                            Integer start,
                            Integer end,
                            Long page, Long pageSize);
}
