package project.shopclone.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shopclone.domain.member.Member;
import project.shopclone.domain.order.entity.Orders;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Optional<Orders> findByMerchantUid(String merchantUid);
    List<Orders> findAllByMemberAndPaymentStatusOrderByOrderDateDesc(Member member, Boolean paymentStatus);
}
