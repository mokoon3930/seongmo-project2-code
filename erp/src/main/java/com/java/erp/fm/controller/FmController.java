package com.java.erp.fm.controller;

import com.java.erp.fm.model.dto.FmDTO;
import com.java.erp.fm.model.vo.TransactionVO;
import com.java.erp.fm.service.FmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FmController {

    private final FmService fmService;

    // 페이지 열기
    @GetMapping("/financialGraph")
    public String financialGraph(Model model) {
        List<FmDTO> fmDTO = new ArrayList<>();

        // DB에서 데이터 가져오기
        List<TransactionVO> transactionVO = fmService.graphSelect();
        for (TransactionVO transactionVOs : transactionVO){
            fmDTO.add(new FmDTO(transactionVOs.getTransType(),
                                transactionVOs.getTransAmount(),
                                transactionVOs.getTransDate(),
                                transactionVOs.getCategory()));
        }

        model.addAttribute("chartData", fmDTO);
        return "/financial/financialGraph"; // 위 Thymeleaf 파일
    }

}
