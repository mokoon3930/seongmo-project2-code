package com.java.erp.fm.model.vo;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemIncomeVO {
    private int iiId;             // 물품 수입ID (PK)
    private int isId;             // 물품 서비스ID (FK → item_service)
    private int itemAmount;       // 물품 금액
    private LocalDate paymentDate;// 결제 날짜
}
