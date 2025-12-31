package com.java.erp.fm.service;

import com.java.erp.commons.model.dto.PagingDTO;
import com.java.erp.commons.model.dto.SearchDTO;
import com.java.erp.fm.dao.FinancialDAO;
import com.java.erp.fm.model.dto.RentalFeeDTO;
import com.java.erp.fm.model.dto.SalaryDTO;
import com.java.erp.fm.model.vo.SalaryVO;
import com.java.erp.fm.model.vo.TransactionVO;
import com.java.erp.hrm.model.vo.EmployeeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FinancialService {

    private final FinancialDAO financialDAO;


    public int total(SearchDTO dto) { return  financialDAO.total(dto); }

    public int salaryTotal(SearchDTO searchDTO){return  financialDAO.salaryTotal(searchDTO);}

    public int locationTotal(SearchDTO searchDTO) {
        return financialDAO.locationTotal(searchDTO);
    }

    public int sendSalaryTotal(SearchDTO searchDTO){return financialDAO.sendSalaryTotal(searchDTO);}

    // 수입 / 지출 회계 기록 조회 페이지
    public List<TransactionVO> selectAll(PagingDTO paging, SearchDTO search) {
        paging.setOffset( paging.getLimit() * ( paging.getPage() - 1 ) );
        return financialDAO.selectAll(paging, search);
    }

    // 직원 급여 조회 페이지
    public List<EmployeeVO> allSalary(PagingDTO paging, SearchDTO searchDTO) {
        paging.setOffset( paging.getLimit() * ( paging.getPage() - 1 ) );
        return financialDAO.allSalary(paging, searchDTO);
    }

    // 직원 급여 수정 페이지 조회
    public EmployeeVO selectSalary(int empId) {
        return financialDAO.selectSalary(empId);
    }

    // 직원 급여 수정
    public int updateSalary(EmployeeVO vo) {
        return financialDAO.updateSalary(vo);
    }

    // 직원 급여 전체 지급
    public int insertSalary(SalaryDTO dto) {
        return financialDAO.insertSalary(dto);
    }

    // 직원 급여 지급 조회
    public List<SalaryVO> totalSalary(PagingDTO paging) {
        paging.setOffset( paging.getLimit() * ( paging.getPage() - 1 ) );
        return financialDAO.totalSalary(paging);
    }

    public List<String> selectDistinctCategories() {
        return financialDAO.selectDistinctCategories();
    }

    public List<String> selectDistinctCategoriesByType(String transType) {
        return financialDAO.selectDistinctCategoriesByType(transType);
    }

    public List<TransactionVO> searchTransactions(Map<String, Object> param, PagingDTO pagingDTO, SearchDTO searchDTO) {
        pagingDTO.setOffset( pagingDTO.getLimit() * ( pagingDTO.getPage() - 1 ) );
        return  financialDAO.searchTransactions(param, pagingDTO, searchDTO);
    }

    // 급여 개인 확인 페이지
    public List<SalaryVO> oneSalary(int empId) {
        return financialDAO.oneSalary(empId);
    }

    // 임대 광고 수수료 조회 페이지
    public List<RentalFeeDTO> selectRentalFee(PagingDTO pagingDTO, SearchDTO searchDTO) {
        pagingDTO.setOffset( pagingDTO.getLimit() * ( pagingDTO.getPage() - 1 ) );
        return financialDAO.selectRentalFee(pagingDTO, searchDTO);
    }


}
