package project.shopclone.domain.order.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
public class OrderCompleteRequest {
    private String merchantUid;
    private String impUid;
}
