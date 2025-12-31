package com.java.erp.sm.service;

import com.java.erp.hrm.model.vo.LeavedVO;
import com.java.erp.sm.dao.CalendarDAO;
import com.java.erp.sm.model.dto.CalendarDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarDAO calendarDAO;

    public List<CalendarDTO> locationRent() {
        return calendarDAO.locationRent();
    }

    public List<LeavedVO> myLeaved(int empId) {
        return calendarDAO.myLeaved(empId);
    }
}
