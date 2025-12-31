package com.java.ecommerce.crm.dao;

import com.java.ecommerce.crm.model.vo.CancelInfoVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CancelInfoDAO {
    void cancelInfoInsert(CancelInfoVO vo);
}
