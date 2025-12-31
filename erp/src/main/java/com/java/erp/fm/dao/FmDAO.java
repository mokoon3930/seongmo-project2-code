package com.java.erp.fm.dao;

import com.java.erp.fm.model.dto.FmDTO;
import com.java.erp.fm.model.vo.TransactionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface FmDAO {

    List<TransactionVO> graphSelect();
}




