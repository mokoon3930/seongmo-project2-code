package com.java.ecommerce.crm.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class NotificationDTO {

    private int notiId;      // noti_id
    private int senderId;    // sender_id
    private int receiverId;  // receiver_id
    private int bookingId;   // booking_id
    private String  message;     // message
    private String  isRead;      // is_read : 'N' / 'Y'
    private LocalDateTime createdAt;   // created_at (나중에 LocalDateTime으로 바꿔도 됨)

    private int rrId;
    private int locationId;
    private int qnaId;

}
