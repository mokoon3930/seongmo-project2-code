package com.java.erp.pm.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfoVO {

    private Integer itemInfoId;
    private String itemName;
    private Integer icId;
    private Integer itemPrice;
    private Integer rentFee;
    private Integer qty;  // 보유수량

}
