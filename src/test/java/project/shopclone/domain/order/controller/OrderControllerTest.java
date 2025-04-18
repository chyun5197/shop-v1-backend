package project.shopclone.domain.order.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;
import project.shopclone.domain.member.entity.Member;
import project.shopclone.domain.member.repository.MemberRepository;
import project.shopclone.domain.order.entity.OrderItem;
import project.shopclone.domain.order.entity.Orders;
import project.shopclone.domain.order.repository.OrderItemRepository;
import project.shopclone.domain.order.repository.OrderRepository;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.product.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class OrderControllerTest {
    RestClient restClient = RestClient.create("http://localhost:8080");
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품 주문 동시성 테스트")
    void orderProductConcurrentTest() throws InterruptedException, ExecutionException {
        // Member 3명 선택
        int userCount = 3;
        List<Member> memberList = memberRepository.findAll().subList(0, userCount);

        // 상품 1개 선택해서 수량 설정
        Long productId = 364L;
        Product product = productRepository.findById(productId).get();
//        product.updateStock(1);
        product.updateStock(10);
        productRepository.save(product);

        // OrderSheet-OrderItem 생성
        List<Orders> orderSheetList = new ArrayList<>();
        int number = 1;
        for(Member member : memberList){
            Orders orderSheet = orderRepository.save(Orders.builder()
                            .member(member)
                            .orderName(String.valueOf(number++))
                            .isPaid(false)
                            .build());
            orderItemRepository.save(OrderItem.builder()
                            .orders(orderSheet)
                            .product(product)
                            .quantity(1)
                            .build());
            orderSheetList.add(orderSheet);
        }

        // 실행전 상품 재고
        int startProductStock = getProductStock(productId);

        // 주문서 3개로 같은 상품 동시에 재고 감소
        ExecutorService executorService = Executors.newFixedThreadPool(20); // 스레드풀 개수
        CountDownLatch countDownLatch = new CountDownLatch(userCount);

        log.info("구매 요청 순서");
        for(Orders orderSheet : orderSheetList){
            executorService.execute(() -> {
                try{
                    log.info("{}번 구매 요청 시작", orderSheet.getOrderName());
                    orderStock(orderSheet.getOrderId());
//                    decreaseStock(productId);
                }catch (Exception e){
                    log.error("Error during removeStock", e);
                }finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();
//        boolean awaitTermination = executorService.awaitTermination(10, TimeUnit.SECONDS);
//        if(!awaitTermination){
//            log.error("스레드풀 타임아웃");
//        }

        // 실행후 상품 재고
        int endProductStock = getProductStock(productId);
        log.info("실행전 상품 재고 = {}개", startProductStock);
        log.info("실행후 상품 재고 = {}개", endProductStock);
        log.info("상품 재고 변화량 = {}개", startProductStock - endProductStock);

//        for(Orders orderSheet : orderRepository.findAll()){
//            log.info("{}번 주문 {}", orderSheet.getOrderName(), orderSheet.getIsPaid() ? "성공":"실패");
//        }

        // 생성한 주문서 삭제
        orderRepository.deleteAll();
    }

    void orderStock(Long orderId){
        restClient.post()
                .uri("/test/order/" + orderId)
                .retrieve()
                .toEntity(Void.class);
    }

    Integer getProductStock(Long productId){
        return restClient.get()
                .uri("/test/product/{productId}/stock", productId)
                .retrieve()
                .body(Integer.class);
    }

    void decreaseStock(Long productId){
        restClient.post()
                .uri("/test/product/{productId}", productId)
                .retrieve()
                .toEntity(Void.class);
    }

}