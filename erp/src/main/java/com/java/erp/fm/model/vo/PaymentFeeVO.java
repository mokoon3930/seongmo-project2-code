package com.java.erp.fm.model.vo;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentFeeVO {
    private int pfId;             // 결제 수수료 ID (PK)
    private int bookingId;        // 예약 ID (FK → booking)
    private int bookingPayment;   // 결제 금액
    private int bookingFee;       // 결제 수수료
    private LocalDate paymentDate;// 결제 날짜
}
