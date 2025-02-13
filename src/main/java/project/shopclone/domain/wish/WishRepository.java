package project.shopclone.domain.wish;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shopclone.domain.member.Member;
import project.shopclone.domain.product.entity.Product;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findAllByMemberOrderByCreatedAtDesc(Member member);
    Wish findByProduct(Product product);
}
