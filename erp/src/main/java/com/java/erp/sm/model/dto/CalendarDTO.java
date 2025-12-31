package com.java.erp.sm.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CalendarDTO {

    private String locationName;
    private Date startDate;
    private Date endDate;
    private Date endPlenDate;
    private String status;

    // ✅ 추가: 예약 ID (이벤트 구분용)
    private int bookingId;

    // ✅ 임대인
    private int landlordId;
    private String landlordName;
    private String landlordPhone;
    private String landlordEmail;

    // ✅ 임차인
    private int tenantId;
    private String tenantName;
    private String tenantPhone;
    private String tenantEmail;

    // ✅ 기간/금액
    private int bookingDays;
    private Integer bookingFee;
    private Integer transportFee;
    private Integer itemFee;

}
