package com.java.erp.fm.model.vo;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseVO {
    private int purchaseId;       // 매입 번호
    private int iiId;             // 물품명 외래키 (item_info)
    private int quantity;         // 수량
    private int varAmount;        // 부가세 총액
    private int totalAmount;      // 총액 (unit_price * quantity)
    private LocalDate purchaseDate; // 매입일
}
