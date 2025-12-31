package com.java.erp.sm.dao;

import com.java.erp.hrm.model.vo.LeavedVO;
import com.java.erp.sm.model.dto.CalendarDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CalendarDAO {


    List<CalendarDTO> locationRent();

    List<LeavedVO> myLeaved(int empId);
}

