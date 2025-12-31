package com.java.erp.fm.controller;

import com.java.erp.commons.model.dto.PagingDTO;
import com.java.erp.commons.model.dto.SearchDTO;
import com.java.erp.fm.model.dto.RentalFeeDTO;
import com.java.erp.fm.model.dto.SalaryDTO;
import com.java.erp.fm.model.vo.SalaryVO;
import com.java.erp.fm.model.vo.TransactionVO;
import com.java.erp.fm.service.FinancialService;
import com.java.erp.hrm.model.vo.EmployeeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class FinancialController {

    private final FinancialService financialService;

    @GetMapping("financial")
    public String financial(Model model, PagingDTO paging, SearchDTO search) {
        List<TransactionVO> transList = financialService.selectAll(paging, search);

        List<String> categories = (search.getTransType() == null || search.getTransType().isBlank())
                ? financialService.selectDistinctCategories()
                : financialService.selectDistinctCategoriesByType(search.getTransType());

        model.addAttribute("transList", transList);
        model.addAttribute("categories", categories);
        model.addAttribute("paging", new PagingDTO( paging.getPage(), financialService.total(search) ) );

        return "financial/financial";
    }


    // 직원 급여 조회 페이지
    @GetMapping("allSalary")
    public String allSalary(Model model, PagingDTO paging, SearchDTO search){
        List<EmployeeVO> vo = financialService.allSalary(paging, search);
        model.addAttribute("vo", vo);
        model.addAttribute("paging", new PagingDTO( paging.getPage(), financialService.salaryTotal(search) ) );
        return "/financial/all_salary";
    }

    // 총원 급여 지급 페이지
    @GetMapping("sendSalary")
    public String sendSalary(Model model, PagingDTO paging, SearchDTO search){
        List<SalaryVO> salary = financialService.totalSalary(paging);
        model.addAttribute("salary", salary);
        model.addAttribute("paging", new PagingDTO( paging.getPage(), financialService.sendSalaryTotal(search) ) );
        return "/financial/send_salary";
    }

    // 급여 개인 확인 페이지
    @GetMapping("selectOne_salary")
    public String selectOneSalary(Model model, int empId){
        List<SalaryVO> vo = financialService.oneSalary(empId);
        model.addAttribute("salary", vo);
        return "/financial/selectOne_salary";
    }

    // 급여 개인 확인 페이지 모달
    @GetMapping("/salary/history")
    public String salaryHistory(@RequestParam("empId") int empId, Model model) {
        // 1. 서비스에서 해당 사원의 급여 내역 조회
        List<SalaryVO> vo = financialService.oneSalary(empId);

        // 2. 모델에 담기
        model.addAttribute("salary", vo); // 변수명 salaryList로 통일

        // 3. 전체 페이지가 아니라 '조각(Fragment)'만 반환!
        // "폴더명/파일이름 :: 조각이름"
        return "financial/salaryHistoryFragment :: historyContent";
    }

    // 직원 급여 수정 페이지 조회
    @GetMapping("/salary/update")
    public String selectSalaryToUpdate(@RequestParam("empId") int empId, Model model){
        System.out.println("empId:"+empId);
        EmployeeVO vo = financialService.selectSalary(empId);
        System.out.println("vo:"+vo);
        model.addAttribute("salary", vo);
        return "financial/salaryUpdateFragment :: updateContent";
    }

//    // 직원 급여 수정 페이지 조회
//    @GetMapping("/salary/update")
//    public String selectSalaryToUpdate(@RequestParam("empId") int empId, Model model){
//        EmployeeVO vo = financialService.selectSalary(empId);
//        model.addAttribute("salary", vo);
//        return "/financial/update_salary";
//    }

    // 직원 급여 수정
    @PostMapping("update_salary")
    @ResponseBody
    public Map<String, Object> updateSalary(EmployeeVO vo){
        financialService.updateSalary(vo);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);

        return response;
    }

    // 직원 급여 전체 지급
    @PostMapping("insert_salary")

    public String insertSalary(SalaryDTO dto){
        financialService.insertSalary(dto);
        return "redirect:/allSalary";
    }

    // 임대 광고 수수료 조회 페이지
    @GetMapping("rentalFee")
    public String rentalFee(Model model, PagingDTO paging, SearchDTO search){
        List<RentalFeeDTO> dto = financialService.selectRentalFee(paging, search);
        model.addAttribute("dto", dto);
        model.addAttribute("paging", new PagingDTO( paging.getPage(), financialService.locationTotal(search) ) );
        return "financial/rental_fee";
    }


    }





