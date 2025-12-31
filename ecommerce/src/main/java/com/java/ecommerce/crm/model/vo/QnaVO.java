package com.java.ecommerce.crm.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QnaVO {

    private int qnaId;
    private int qnaUserId;         // 작성자
    private String qnaTitle;
    private String qnaContent;
    private int locationId;        // 작성 건물
    private String status;
    private String isSecret;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

}
