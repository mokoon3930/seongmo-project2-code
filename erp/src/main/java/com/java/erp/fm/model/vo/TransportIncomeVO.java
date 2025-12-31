package com.java.erp.fm.model.vo;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransportIncomeVO {
    private int tiId;             // 운송 수입ID (PK)
    private int tsId;             // 운송ID (FK → transport_service)
    private int transportAmount;  // 운송 금액
    private LocalDate paymentDate;// 결제 날짜
}
