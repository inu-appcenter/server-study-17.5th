package server.dongmin.domain.order.enums;

public enum OrderStatus {

    PENDING,    // 대기중
    ACCEPTED,   // 주문 접수
    COOKING,    // 조리중
    DELIVERING, // 배달중
    DELIVERED,  // 배달 완료
    CANCELLED,  // 취소

}
