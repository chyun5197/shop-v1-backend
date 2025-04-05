package project.shopclone.domain.order.dto.request;


import lombok.Getter;

import java.util.List;

@Getter
public class OrderPrepareRequest {
    private String merchantUid;
    private String email;
    private String orderName;
    private List<OrderItemRequest> orderSheetItems;
}
