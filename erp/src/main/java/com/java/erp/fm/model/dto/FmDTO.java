package com.java.erp.fm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FmDTO {

    private String transType;
    private int transAmount;
    private LocalDate transDate;
    private String category;

}
