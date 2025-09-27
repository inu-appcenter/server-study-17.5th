package server.dongmin.domain.order.entity;

public enum OrderStatus {

    Pending, // 대기중
    Accepted, // 주문 접수
    Delivering, // 배달중
    Delivered, // 배달 완료
    Cancelled, // 취소

}
