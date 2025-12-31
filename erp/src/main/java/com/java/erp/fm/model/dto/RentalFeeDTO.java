package com.java.erp.fm.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RentalFeeDTO {

    private int rrId;
    private int locationId;
    private String locationName;
    private String rentStart;
    private String rentEnd;
    private int rentalFee;
    private int rentDays;
    private int adCost;

}
