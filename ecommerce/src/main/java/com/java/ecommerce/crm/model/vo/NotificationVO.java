package com.java.ecommerce.crm.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationVO {
    private int notiId;      // noti_id
    private int senderId;    // sender_id
    private int receiverId;  // receiver_id
    private int bookingId;   // booking_id
    private String  message;     // message
    private String  isRead;      // is_read : 'N' / 'Y'
    private LocalDateTime createdAt;   // created_at (나중에 LocalDateTime으로 바꿔도 됨)

}
