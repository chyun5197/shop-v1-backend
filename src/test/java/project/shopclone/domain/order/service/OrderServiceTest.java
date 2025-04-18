package project.shopclone.domain.order.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.shopclone.domain.order.entity.OrderItem;
import project.shopclone.domain.order.entity.Orders;
import project.shopclone.domain.order.repository.OrderItemRepository;
import project.shopclone.domain.order.repository.OrderRepository;
import project.shopclone.domain.product.entity.Product;
import project.shopclone.domain.product.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Slf4j
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderItemRepository orderItemRepository;
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    OrderService orderService;

    @Test
    @DisplayName("상품 주문 동시성 테스트")
    void orderProductConcurrentTest(){
        // given
        // 3개의 주문이 동시에 같은 상품 구매 시도
        List<Orders> orderSheetList = new ArrayList<>();
        Product product = createProduct();
        log.info("초기 상품 재고: " + product.getStock());
        for (int i = 0; i < 3; i++) {
            orderSheetList.add(createOrderSheet(product));
        }
        given(product.getStock()).willReturn(product.getStock());
        given(product.getId()).willReturn(product.getId());

        // when
        for(Orders orderSheet : orderSheetList){
            for(OrderItem orderItem: orderSheet.getOrderItemList()){
                orderService.redissonDecreaseStock(orderItem.getProduct().getId(), orderItem.getQuantity());
            }
        }

        // then
        log.info("구매후 상품 재고: " + product.getStock());
        verify(product).removeStock(1);
    }

    private Orders createOrderSheet(Product product){
        Orders orderSheet = mock(Orders.class);
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(createOrderItem(orderSheet, product));
        given(orderSheet.getOrderItemList()).willReturn(orderItemList);
        return orderSheet;
    }

    private OrderItem createOrderItem(Orders orderSheet, Product product){
        OrderItem orderItem = mock(OrderItem.class);
//        given(orderItem.getOrders()).willReturn(orderSheet);
        given(orderItem.getProduct()).willReturn(product);
        given(orderItem.getQuantity()).willReturn(1);
        return orderItem;
    }

    private Product createProduct(){
        Product product = mock(Product.class);
        return product;
    }
}