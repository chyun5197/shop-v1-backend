package project.shopclone.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shopclone.domain.order.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
