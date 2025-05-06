package project.shopclone.domain.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shopclone.domain.member.entity.Member;
import project.shopclone.domain.member.service.MemberService;
import project.shopclone.domain.order.dto.request.OrderItemRequest;
import project.shopclone.domain.order.dto.response.OrderItemSummary;
import project.shopclone.domain.order.dto.response.OrderMemberInfo;
import project.shopclone.domain.order.dto.response.OrderSheetResponse;
import project.shopclone.domain.order.dto.response.orderlist.OrderListResponse;
import project.shopclone.domain.order.entity.OrderItem;
import project.shopclone.domain.order.entity.Orders;
import project.shopclone.domain.order.entity.Payment;
import project.shopclone.domain.order.exception.OrderErrorCode;
import project.shopclone.domain.order.exception.OrderException;
import project.shopclone.domain.order.repository.OrderRepository;
import project.shopclone.domain.order.repository.PaymentRepository;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.product.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final MemberService memberService;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final RedissonClient redissonClient;

    // 주문번호, 주문자 정보, 결제 정보 응답
    public ResponseEntity<OrderSheetResponse> createOrderSheet(String token, List<OrderItemRequest> orderItemRequestList) {
        Member member = memberService.getMember(token);

        // OrderItem -> OrderItemSummary 변환 -> List<OrderItemSummary> 추가
        List<OrderItemSummary> orderItemSummaryList = new ArrayList<>();
        for(OrderItemRequest orderItemRequest : orderItemRequestList ){
            Product product = productRepository.findById(orderItemRequest.getProductId()).orElseThrow();
            orderItemSummaryList.add(OrderItemSummary.of(product, orderItemRequest.getQuantity()));
        }

        // Member, List<OrderItemSummary> 담아서 응답
        return ResponseEntity.ok(OrderSheetResponse.builder()
                .orderMemberInfo(OrderMemberInfo.from(member))
                .orderItemSummaryList(orderItemSummaryList)
                .merchantId("Payment"+String.valueOf(UUID.randomUUID()).substring(0, 8)) // 주문번호 생성
                .build());
    }

    // 주문조회
    public ResponseEntity<List<OrderListResponse>> getOrderList(String token) {
        Member member = memberService.getMember(token);
        List<Orders> orderList = orderRepository.findAllByMemberAndIsPaidOrderByOrderDateDesc(member, true);
        List<OrderListResponse> orderListResponses = new ArrayList<>();
        Payment payment;
        for(Orders order : orderList){
            payment = paymentRepository.findByOrdersAndOrderCompleted(order, true);
            orderListResponses.add(OrderListResponse.of(order, payment));
        }

        return ResponseEntity.ok(orderListResponses);
    }

    // 상품 재고 감소
//    @Transactional
//    public void decreaseStock(Orders orderSheet){
//        List<OrderItem> orderItemList  = orderSheet.getOrderItemList();
//        for(OrderItem orderItem : orderItemList){
//            Product product = orderItem.getProduct();
//            Integer quantity = orderItem.getQuantity();
//
//            // 주문하는 사이에 다른 구매자에 의해 재고가 줄어들어 구매수량 만큼의 주문이 불가능한 상황
//
//            product.decreaseStock(quantity);
////            redissonRemoveStock(product.getId(), quantity);
////            if(!redissonRemoveStock(product.getId(), quantity)){
////                throw new OrderException(OrderErrorCode.OUT_OF_STOCK);
////            }
//            if (!product.removeStock(quantity)){
//                throw new OrderException(OrderErrorCode.OUT_OF_STOCK);
//            }
//        }
//
//        // (테스트용) 주문결제 완료
//        orderSheet.updateIsPaid(true);
//    }

    // 분산락으로 재고 감소
//    @Transactional // !트랜잭션 걸면 안됨
    // 트랜잭션 시작하고 락을 획득하면 안된다.
    // 트랜잭션 정합성에 의해 커밋 이전의 데이터(수량)를 참조하므로 다른 사용자가 변경한 수량이 반영 안되기 때문이다.
    public void redissonDecreaseStock(Long productId, Integer quantity){
        String lockKey = "lock" + productId; // 분산 락 키 생성
        RLock lock = redissonClient.getLock(lockKey); // RLock 객체 생성
        try {
            // 락 획득 시도
            if (lock.tryLock(5, 3, TimeUnit.SECONDS)) { // 최대 대기 시간 5초, 락 유지(TTL) 3초
                // 상품 재고 감소
                Product product = productRepository.findById(productId).orElseThrow();
//                Hibernate.initialize(product); // 필요한 시점에 명시적으로 로드
                product.decreaseStock(quantity);
                productRepository.save(product);
                log.info("if 실행: {}", Thread.currentThread().getName());
            }
        } catch (Exception e) {
            log.error("락 획득에서 에러 발생 : ", e);
        }finally {
            if (lock.isHeldByCurrentThread()) { // 현재 스레드가 락을 소유하고 있으면 락 해제
                lock.unlock();
            }
        }
    }

    // 주문완료 후 처리: 장바구니에서 상품 삭제, 적립금 업데이트, 알림 전송
    @Transactional
    public void OrderComplete(){
        
    }
}
