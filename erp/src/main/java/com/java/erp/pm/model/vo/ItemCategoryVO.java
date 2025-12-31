package com.java.erp.pm.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

@Alias("ic")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCategoryVO {

    private Integer icId;
    private String category;

}
