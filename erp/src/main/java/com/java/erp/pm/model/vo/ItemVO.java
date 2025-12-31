package com.java.erp.pm.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemVO {

    private Integer itemId;
    private Integer itemInfoId;
    private Date itemStoreDate;
    private Integer isId;

}
