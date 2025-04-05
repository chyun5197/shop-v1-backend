package project.shopclone.domain.wish;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shopclone.domain.member.entity.Member;
import project.shopclone.domain.product.entity.Product;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findAllByMemberOrderByCreatedAtDesc(Member member);
    Wish findByProduct(Product product);
    Optional<Wish> findByMemberAndProduct(Member member, Product product);

//    @Query("select p from Product p where p.id not in " +
//            "(select w.member from Wish w where w.member = :member)")
//    @Query(value = "select * from product where id not in " +
//            "(select member_id from wish where member_id = :memberId) " +
//            "order by Rand() limit 1",
//    nativeQuery = true)
//    List<Wish> findListOneNotInOriginWish(Long memberId);
}
