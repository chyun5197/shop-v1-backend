package project.shopclone.domain.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shopclone.domain.cart.entity.Cart;
import project.shopclone.domain.cart.entity.CartItem;
import project.shopclone.domain.member.Member;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.wish.Wish;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByProduct(Product product);
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

}
