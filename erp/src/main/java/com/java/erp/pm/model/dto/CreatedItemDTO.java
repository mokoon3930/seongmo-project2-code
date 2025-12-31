package com.java.erp.pm.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CreatedItemDTO {

    private Integer icId;
    private String category;

    private Integer itemInfoId;
    private String itemName;
    private Integer itemPrice;
    private Integer rentFee;

    private Integer itemId;
    private Date itemStoreDate;

}
