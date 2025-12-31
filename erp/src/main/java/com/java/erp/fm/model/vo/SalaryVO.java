package com.java.erp.fm.model.vo;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryVO {
    private int salaryId;         // 급여ID (PK)
    private int empId;            // 직원ID (FK → employee.emp_id)
    private int deduction;        // 공제금액
    private int totalSalary;      // 총 지급액
    private LocalDate salaryDate; // 지급일
}
