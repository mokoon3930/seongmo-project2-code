package com.java.ecommerce.crm.service;

import com.java.ecommerce.crm.dao.NotificationDAO;
import com.java.ecommerce.crm.model.dto.NotificationDTO;
import com.java.ecommerce.crm.model.vo.QnaVO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationDAO notificationDAO;
    private final SimpMessagingTemplate messagingTemplate; // 웹소켓 전송용

    // 알림 목록 조회 (지금 /notifications 에서 사용)
    public List<NotificationDTO> selectNoti(int receiverId) {

        return notificationDAO.selectNoti(receiverId);

    }



    //  실시간 알림(WebSocket) 전송 전용
    public void sendRealTimeNotification(NotificationDTO vo) {

    /*  System.out.println(" [NotificationService] sendRealTimeNotification 호출됨");
        System.out.println("   receiverId=" + vo.getReceiverId()
                + ", message=" + vo.getMessage());*/

        String destination = "/topic/notifications/" + vo.getReceiverId();
     /*   System.out.println("   ▶ WebSocket 전송: destination=" + destination);*/

        messagingTemplate.convertAndSend(destination, vo);
    }

    public boolean updateIsRead(int notiId) {
        notificationDAO.updateIsRead(notiId, "Y");
        return false;
    }

    public void notiAllDelete(int receiverId) {
        notificationDAO.notiAllDelete(receiverId);
    }

    public int notiSelectDelete(int notiId) {
        return notificationDAO.notiSelectDelete(notiId);
    }

    // 🔥 방금 만든 예약 건의 알림 1건 조회
    public NotificationDTO findLatestByBookingId(int bookingId) {
        return notificationDAO.findLatestByBookingId(bookingId);
    }
}
