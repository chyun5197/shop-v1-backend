package project.shopclone.domain.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.shopclone.domain.member.Member;
import project.shopclone.domain.member.MemberService;
import project.shopclone.domain.order.dto.request.OrderItemRequest;
import project.shopclone.domain.order.dto.response.OrderItemSummary;
import project.shopclone.domain.order.dto.response.OrderMemberInfo;
import project.shopclone.domain.order.dto.response.OrderSheetResponse;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.product.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final MemberService memberService;
    private final ProductRepository productRepository;

    // 주문 정보 저장 / 주문자 정보, 결제 정보 응답
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
                .merchantId("payement-"+String.valueOf(UUID.randomUUID()).substring(0, 13))
                .build());
    }

}
