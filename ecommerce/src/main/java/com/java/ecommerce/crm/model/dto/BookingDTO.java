package com.java.ecommerce.crm.model.dto;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Alias("bookingDTO")
public class BookingDTO {
    private int rrId;
    private int userId;
    private LocalDate bookingStart;
    private LocalDate bookingEnd;
    private String rentPurpose;
    private int locationRentFee;
    private boolean transport;
    private boolean items;
    private LocalDate tsStartDate;
    private LocalDate tsEndDate;
    private int truckNum;
    private int transportFee;
    private LocalDate itemStartDate;
    private LocalDate itemEndDate;
    private List<RentItemsDTO> rentItems; // [{...}, {...}] 그대로
    private int totalRentFee;
    private String orderId;
    private String paymentKey;
    private int amount;

    // 건물 이름
    private String locationName;
    // booking 날짜
    private String bookingPeriod;
    // 작성날자
    private LocalDateTime createdAt;
    // 건물 ID
    private int locationId;

    private boolean isCanceled;
    private LocalDateTime cancellationDate;

    private String thumbnail;
    private int bookingId;
}
