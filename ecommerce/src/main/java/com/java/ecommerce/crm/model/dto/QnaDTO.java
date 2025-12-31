package com.java.ecommerce.crm.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QnaDTO {

    private int qnaId;
    private int qnaUserId;
    private int locationId;
    private String qnaTitle;
    private String qnaContent;
    private String userName;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 날자 format
    private LocalDateTime qnaCreatedAt;
    private String status;   // WAIT / DONE / HIDDEN 등
    private String isSecret; // 'Y' / 'N'

    private int answerId;
    private int answerUserId;
    private String answerContent;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime answerCreatedAt;

    private String locationName;

}
