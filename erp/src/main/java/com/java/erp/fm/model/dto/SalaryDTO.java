package com.java.erp.fm.model.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
public class SalaryDTO {

    // employee
    private int empId;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private int deptId;
    private int jobId;
    private LocalDate hireDate;
    private LocalDate resignDate;
    private BigDecimal baseSalary;
    private String status;
    private int totalDays;
    private  String role;

    // 지급일 설정
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate payDate;

    // salary
    private int salaryId;         // 급여ID (PK)
    private int deduction;        // 공제금액
    private int totalSalary;      // 총 지급액
    private LocalDate salaryDate; // 지급일
}
