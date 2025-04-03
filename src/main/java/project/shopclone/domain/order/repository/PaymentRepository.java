package project.shopclone.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shopclone.domain.order.entity.Orders;
import project.shopclone.domain.order.entity.Payment;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByMerchantUid(String merchantUid);
    Payment findByOrdersAndOrderCompleted(Orders orders, boolean orderCompleted);
}
