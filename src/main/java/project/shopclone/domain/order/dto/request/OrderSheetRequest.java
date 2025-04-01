package project.shopclone.domain.order.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderSheetRequest {
    private List<OrderItemRequest> OrderItemRequestList;
}
