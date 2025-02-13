package project.shopclone.domain.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shopclone.domain.cart.entity.CartItem;
import project.shopclone.domain.product.entity.Product;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByProduct(Product product);
}
