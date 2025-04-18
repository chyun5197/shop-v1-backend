package project.shopclone.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VerificationResult { // 결제 검증 결과
    boolean isCorrect; // DB 금액과 실제 금액 일치 여부
    com.siot.IamportRestClient.response.Payment portOneResponse; // 포트원으로부터 받은 결제정보 응답
}
