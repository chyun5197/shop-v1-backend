package project.shopclone.domain.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OrderSheetResponse {
    List<OrderItemSummary> orderItemSummaryList;
    OrderMemberInfo orderMemberInfo;
    String merchantId;
}
