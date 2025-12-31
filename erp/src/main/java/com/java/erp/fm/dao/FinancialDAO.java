package com.java.erp.fm.dao;

import com.java.erp.commons.model.dto.PagingDTO;
import com.java.erp.commons.model.dto.SearchDTO;
import com.java.erp.fm.model.dto.RentalFeeDTO;
import com.java.erp.fm.model.dto.SalaryDTO;
import com.java.erp.fm.model.vo.SalaryVO;
import com.java.erp.fm.model.vo.TransactionVO;
import com.java.erp.hrm.model.vo.EmployeeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface FinancialDAO {

    int total(SearchDTO search);

    int salaryTotal (@Param("search")SearchDTO searchDTO);

    int locationTotal(@Param("search")SearchDTO searchDTO);

    int sendSalaryTotal(@Param("search")SearchDTO searchDTO);

    // 수입 / 지출 회계 기록 조회 페이지
    List<TransactionVO> selectAll(@Param("paging") PagingDTO paging, @Param("search") SearchDTO search);

    // 직원 급여 조회 페이지
    List<EmployeeVO> allSalary(PagingDTO paging, SearchDTO search);

    // 직원 급여 수정 페이지 조회
    EmployeeVO selectSalary(int empId);

    // 직원 급여 수정
    int updateSalary(EmployeeVO vo);

    // 직원 급여 전체 지급
    int insertSalary(SalaryDTO dto);

    // 직원 급여 지급 조회
    List<SalaryVO> totalSalary( @Param("paging") PagingDTO pagingDTO );

    List<String> selectDistinctCategories();

    List<String> selectDistinctCategoriesByType(String transType);

    List<TransactionVO> searchTransactions(Map<String, Object> param, @Param("paging") PagingDTO pagingDTO, @Param("search") SearchDTO searchDTO);

    // 개인 급여 확인 페이지
    List<SalaryVO> oneSalary(int empId);

    List<RentalFeeDTO> selectRentalFee(@Param("paging") PagingDTO pagingDTO, @Param("search") SearchDTO searchDTO);


}
