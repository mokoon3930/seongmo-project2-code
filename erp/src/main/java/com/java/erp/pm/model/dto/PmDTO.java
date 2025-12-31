package com.java.erp.pm.model.dto;

import com.java.erp.pm.model.vo.PmVO;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Data
public class PmDTO {

    private Integer itemInfoId;
    private String itemName;
    private Integer icId;
    private Integer itemPrice;
    private Integer rentFee;
    private Integer totalQty;  // 보유수량
}
