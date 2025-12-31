package com.java.erp.sm.controller;


import com.java.erp.hrm.model.vo.EmployeeVO;
import com.java.erp.hrm.model.vo.LeavedVO;
import com.java.erp.sm.model.dto.CalendarDTO;
import com.java.erp.sm.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/calendar")
    public String calender(){ return "/sm/calendar"; }

    @GetMapping("/locationRent")
    @ResponseBody
    public List<CalendarDTO> locationRent() {

        return calendarService.locationRent();
    }

    // 캘린더용
    @GetMapping("/myLeaved")
    @ResponseBody
    public List<LeavedVO> myLeaved(@AuthenticationPrincipal EmployeeVO emp){
            return calendarService.myLeaved(emp.getEmpId());

    }





}
