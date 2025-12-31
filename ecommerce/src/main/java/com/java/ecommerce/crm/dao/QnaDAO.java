package com.java.ecommerce.crm.dao;

import com.java.ecommerce.crm.model.dto.QnaDTO;
import com.java.ecommerce.crm.model.vo.QnaAnswerVO;
import com.java.ecommerce.crm.model.vo.QnaVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QnaDAO {

    List<QnaDTO> qnaAllSelect(int locationId);

    int qnaregister(QnaDTO qnaDTO);

    int qnaAnswerInsert(QnaDTO qnaDTO);

    void updateStatusToDone(@Param("qnaId") int qnaId);

    Integer findHostId(int locationId);

    Integer findQnaUserId(int qnaId);

    List<QnaDTO> myWriteQna(int userId);

    int countQnaWait(int locationId);
}
