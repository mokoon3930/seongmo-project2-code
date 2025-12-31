package com.java.ecommerce.crm.service;

import com.java.ecommerce.crm.dao.QnaDAO;
import com.java.ecommerce.crm.model.dto.NotificationDTO;
import com.java.ecommerce.crm.model.dto.QnaDTO;
import com.java.ecommerce.crm.model.vo.QnaAnswerVO;
import com.java.ecommerce.crm.model.vo.QnaVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaDAO qnaDAO;
    private final NotificationService notificationService;

    public List<QnaDTO> qnaAllSelect(int locationId) {
        return qnaDAO.qnaAllSelect(locationId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void qnaregister(QnaDTO qnaDTO) {
        int result = qnaDAO.qnaregister(qnaDTO);


        Integer tenantId  = qnaDAO.findHostId(qnaDTO.getLocationId());

        System.out.println(tenantId);
        NotificationDTO noti = new NotificationDTO();
        noti.setReceiverId(tenantId);

        notificationService.sendRealTimeNotification(noti);


    }

    @Transactional(rollbackFor = Exception.class)
    public void qnaAnswerInsert(QnaDTO qnaDTO) {
        qnaDAO.qnaAnswerInsert(qnaDTO);
        qnaDAO.updateStatusToDone(qnaDTO.getQnaId());

        Integer tenantId  = qnaDAO.findQnaUserId(qnaDTO.getQnaId());

        System.out.println(tenantId);
        NotificationDTO noti = new NotificationDTO();
        noti.setReceiverId(tenantId);

        notificationService.sendRealTimeNotification(noti);

    }

    public List<QnaDTO> myWriteQna(int userId) {
        return qnaDAO.myWriteQna(userId);
    }

    public int countQnaWait(int locationId) {return qnaDAO.countQnaWait(locationId);
    }
}
