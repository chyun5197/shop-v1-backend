package project.shopclone.global.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import project.shopclone.domain.order.entity.OrderItem;
import project.shopclone.domain.order.entity.Orders;
import project.shopclone.domain.order.repository.OrderRepository;
import project.shopclone.domain.order.service.OrderService;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.product.repository.ProductRepository;

import java.util.Random;
import java.util.UUID;

@Slf4j
@Tag(name = "테스트용 API")
@RequiredArgsConstructor
@RestController
public class Test {
    // 로드밸런싱으로 서버가 바뀌면 응답도 바뀌도록
    public static final UUID randomUUID = UUID.randomUUID();

    private final HttpSession httpSession;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;


    @Operation(summary = "헬스 체크")
    @GetMapping("/health")
    public String healthCheck() {
        return "Success Health Check";
    }

    @PostMapping("/test/session/{value}")
    public void putValue(@PathVariable int value) {
        httpSession.setAttribute("value", value);
    }

    @GetMapping("/test/session")
    public ResponseEntity<Integer> getValue() {
        Integer value = (Integer) httpSession.getAttribute("value");
        return ResponseEntity.ok(value);
    }

    @GetMapping("/test/balancer")
    public ResponseEntity<UUID> test() {
        return ResponseEntity.ok(randomUUID);
    }

    @GetMapping("/test/delay")
    public String testDelay() {
        int[] arr = new int[10000000]; // 천만 0.15초(로컬 응답시간)
//        int[] arr = new int[100000000]; // 일억 1.5초
        Random rd = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rd.nextInt(10000);
        }

        return "fin";
    }

    // 주문 동시성 테스트
    @PostMapping("/test/order/{orderId}")
    public void orderStock(@PathVariable Long orderId) {
        Orders orderSheet = orderRepository.findById(orderId).get();
        for(OrderItem orderItem : orderSheet.getOrderItemList()){
            Long productId = orderItem.getProduct().getId();
            Integer quantity = orderItem.getQuantity();
            log.info("name = {}, productId = {}, quantity = {}", Thread.currentThread().getName(), productId, quantity);
            orderService.redissonDecreaseStock(productId,quantity );
        }
//        OrderItem orderItem = orderSheet.getOrderItemList().get(0);
//        Long productId = orderItem.getProduct().getId();
//        Integer quantity = orderItem.getQuantity();
//        orderService.redissonDecreaseStock(productId,quantity);
    }
    // 상품 재고 확인
    @GetMapping("/test/product/{productId}/stock")
    public Integer getProductStock(@PathVariable Long productId) {
        return productRepository.findById(productId).get().getStock();
    }

    // 상품 재고 감소 동시성 테스트
    @PostMapping("/test/product/{productId}")
    public void decreaseStock(@PathVariable Long productId){
        orderService.redissonDecreaseStock(productId, 1);
    }
}
