package project.shopclone.domain.order.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderPrepareRequest {
    private String merchantUid;
    private String email;
    private String orderName;
    private List<OrderItemRequest> orderSheetItems;
}
