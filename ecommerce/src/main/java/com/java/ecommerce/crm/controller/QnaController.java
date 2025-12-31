package com.java.ecommerce.crm.controller;

import com.java.ecommerce.crm.model.dto.QnaDTO;
import com.java.ecommerce.crm.model.vo.QnaAnswerVO;
import com.java.ecommerce.crm.model.vo.UserVO;
import com.java.ecommerce.crm.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;

    @GetMapping("/qnaSelect")
    public List<QnaDTO> qnaAllSelect(int locationId){
        System.out.println(qnaService.qnaAllSelect(locationId));
        return qnaService.qnaAllSelect(locationId);
    }

    @PostMapping("/qna/register")
    public String qnaregister(@RequestBody QnaDTO qnaDTO){
        qnaService.qnaregister(qnaDTO);
        return "ok";
    }

    @PostMapping("/qnaAnswerInsert")
    public String qnaAnswerInsert( @RequestBody QnaDTO qnaDTO, Authentication authentication ){

        // 1. 현재 세션에서 User ID를 가져옵니다.
        UserVO sessionVo = (UserVO) authentication.getPrincipal();
        int userId = sessionVo.getUserId(); // User ID를 사용하여 DB 조회
        qnaDTO.setAnswerUserId(userId);


        qnaService.qnaAnswerInsert(qnaDTO);
        return "OK";
    }

    @GetMapping("/countQnaWait")
    public int countQnaWait(@RequestParam("locationId") int locationId) {
        return qnaService.countQnaWait(locationId);
    }



}
