package com.java.erp.fm.model.vo;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionVO {
    private int transId;          // 거래 번호
    private String transType;     // 수입/지출 ('수입', '지출')
    private int transAmount;      // 금액
    private String category;      // 분류
    private String transDesc;     // 상세 내역
    private LocalDate transDate;  // 발생 일자
}
