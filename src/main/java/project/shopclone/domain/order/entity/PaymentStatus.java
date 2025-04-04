package project.shopclone.domain.order.entity;

public enum PaymentStatus {
    // 결제 대기, 실패, 완료, 취소
    READY, FAILED, PAID, CANCEL
}
