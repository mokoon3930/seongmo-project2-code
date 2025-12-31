package com.java.ecommerce.crm.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QnaAnswerVO {

    private int answerId;
    private int qnaId;
    private int answerUserId;
    private String answerContent;
    private LocalDateTime createdAt;
}
