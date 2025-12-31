package com.java.erp.fm.service;

import com.java.erp.fm.dao.FmDAO;
import com.java.erp.fm.model.dto.FmDTO;
import com.java.erp.fm.model.vo.TransactionVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FmService {

    private final FmDAO fmDAO;


    public List<TransactionVO> graphSelect() {
        return fmDAO.graphSelect();
    }
}
