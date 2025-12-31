package com.java.ecommerce.crm.dao;

import com.java.ecommerce.crm.model.dto.NotificationDTO;
import com.java.ecommerce.crm.model.vo.NotificationVO;
import com.java.ecommerce.crm.model.vo.QnaVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationDAO {

    // 🔹 receiverId 기준 알림 목록 가져오기
    List<NotificationDTO> selectNoti(@Param("receiverId") int receiverId);


    void updateIsRead(@Param("notiId") int notiId,
                      @Param("isRead") String isRead);

    void notiAllDelete(int receiverId);

    int notiSelectDelete(int notiId);

    NotificationDTO findLatestByBookingId(@Param("bookingId") int bookingId);
}
