package project.shopclone.domain.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shopclone.domain.cart.entity.Cart;
import project.shopclone.domain.member.Member;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByMember(Member member);
}
