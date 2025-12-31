package com.java.ecommerce.crm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.ecommerce.commons.service.PaymentService;
import com.java.ecommerce.crm.dao.BookingDAO;
import com.java.ecommerce.crm.model.dto.*;
import com.java.ecommerce.crm.model.vo.BookingVO;
import com.java.ecommerce.crm.model.vo.ItemServiceVO;
import com.java.ecommerce.crm.model.vo.ServiceManagementVO;
import com.java.ecommerce.crm.model.dto.PaymentInfoDTO;
import com.java.ecommerce.crm.model.vo.*;
import com.java.ecommerce.exceptions.CustomCancellationException;
import com.java.ecommerce.pm.service.ItemService;
import com.java.ecommerce.pm.service.ProjectService;
import com.java.ecommerce.pm.service.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookingService {
    private final BookingDAO bookingDAO;
    private final ServiceManagementService serviceManagementService;
    private final TransportServiceService transportServiceService;
    private final ItemServiceService itemServiceService;
    private final ProjectService projectService;
    private final ItemService itemService;
    private final PaymentService paymentService;
    private final PaymentInfoService paymentInfoService;
    private final CancelInfoService cancelInfoService;
    private final TransportService transportService;

    private final NotificationService notificationService; // 🔔 추가

    @Transactional(rollbackFor = Exception.class)
    public PaymentInfoDTO bookingRegister(BookingDTO bookingDTO) {
        boolean useService = bookingDTO.isTransport() || bookingDTO.isItems();

        if (!bookingDTO.isTransport()) {
            bookingDTO.setTransportFee(0);
        }

        if (!bookingDTO.isItems()) {
            bookingDTO.setTotalRentFee(0);
        }


        String res = paymentService.paymentApproval(bookingDTO.getPaymentKey(), bookingDTO.getOrderId(), Integer.toString(bookingDTO.getAmount()));
        ObjectMapper om = new ObjectMapper();
        PaymentInfoDTO paymentInfoDTO = new PaymentInfoDTO();
        try {
            paymentInfoDTO = om.readValue(res, PaymentInfoDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (paymentInfoDTO.getCode() != null) throw new RuntimeException();
        System.out.println(paymentInfoDTO);
        paymentInfoDTO.setPaymentCategory("대여등록");
        paymentInfoDTO.setUserId(bookingDTO.getUserId());

        BookingVO bookingVO =
                BookingVO.builder()
                        .rrId(bookingDTO.getRrId())
                        .userId(bookingDTO.getUserId())
                        .bookingStart(bookingDTO.getBookingStart())
                        .bookingEnd(bookingDTO.getBookingEnd())
                        .rentPurpose(bookingDTO.getRentPurpose())
                        .useService(useService)
                        .bookingFee(bookingDTO.getLocationRentFee())
                        .paymentKey(bookingDTO.getPaymentKey())
                        .totalFee(bookingDTO.getLocationRentFee() + bookingDTO.getTotalRentFee() + bookingDTO.getTransportFee())
                        .build();
        bookingDAO.bookingRegister(bookingVO);
        System.out.println(bookingVO);

        ServiceManagementVO serviceManagementVO = new ServiceManagementVO();

        if (useService) {
            serviceManagementVO = serviceManagementService.RegisterServiceManagement(bookingDTO, bookingVO);
            System.out.println(serviceManagementVO);
            if (bookingDTO.isTransport()) {
                transportServiceService.registerTransportService(bookingDTO, serviceManagementVO);
            }
            if (bookingDTO.isItems()) {
                ItemServiceVO itemServiceVO = itemServiceService.registerItemService(bookingDTO, serviceManagementVO);
                itemService.itemRentRegister(bookingDTO, itemServiceVO);
            }

            projectService.registerProject(bookingVO, serviceManagementVO);
        }

        paymentInfoService.paymentInfoRegister(paymentInfoDTO);

        // 6-1) rrId → 임대인 정보 / 예약 기간 등 조회 (기존 그대로 사용)
        BookingDTO dto = bookingDAO.findUserIdByRrId(bookingDTO.getRrId());
        //String loName = dto.getLocationName();
        Integer receiverId = dto.getUserId();        // 임대인
        //Integer senderId = bookingDTO.getUserId(); // 예약자
        //int locationId = dto.getLocationId();    // rr_id 기준 location_id

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //String createdAtStr = dto.getCreatedAt().format(formatter);

        /*String msg =
                "새 예약 승인 요청.\n"
                        + "예약 건물: " + loName + "\n"
                        + "예약 기간: " + dto.getBookingPeriod() + "\n"
                        + createdAtStr;
*/
        // 6-2) ★ 트리거가 INSERT 해준 notification 레코드 다시 SELECT
        //      => bookingVO 는 insert 이후에 bookingId 가 채워져 있다고 로그에 나왔음
        //NotificationDTO noti = notificationService.findLatestByBookingId(bookingVO.getBookingId());
            NotificationDTO noti = new NotificationDTO();
            noti.setReceiverId(receiverId);
        /*// 6-3) message 만 우리가 만든 예쁜 문구로 덮어쓰기
        noti.setMessage(msg);

        // 혹시 트리거에서 안 넣어줬다면 안전하게 세팅(중복이면 어차피 같은 값)
        noti.setReceiverId(receiverId);
        noti.setSenderId(senderId);
        noti.setRrId(dto.getRrId());
        noti.setLocationId(locationId);

        System.out.println("💥 [BookingService] WebSocket 알림 발사 직전, receiverId = " + receiverId);
        System.out.println("   ▶ notiId = " + noti.getNotiId());
*/
        // 6-4) WebSocket 발사
        notificationService.sendRealTimeNotification(noti);
        return paymentInfoDTO;
    }

    public List<BookingInfoDTO> showBookingInfo(BookingVO bookingVO) {
        System.out.println(bookingDAO.showBookingInfo(bookingVO));
        return bookingDAO.showBookingInfo(bookingVO);

    }

    @Transactional(rollbackFor = Exception.class)
    public void bookingApprove(BookingVO bookingVO) {
        bookingVO.setRentApproval(true);
        bookingDAO.bookingApprove(bookingVO);

        // 2) 임차인/예약번호 가져오기
        Integer tenantId  = bookingVO.getUserId();     // 프론트에서 같이 넘어온 userId
        //Integer bookingId = bookingVO.getBookingId();  // 프론트에서 넘어온 bookingId

        // 3) 메시지 구성
        //String message = "예약이 승인되었습니다. 예약번호: " + bookingId;

        // 4) 소켓으로 보낼 DTO 생성
        NotificationDTO noti = new NotificationDTO();
        noti.setReceiverId(tenantId);
        /*noti.setBookingId(bookingId);
        noti.setMessage(message);
        noti.setIsRead("N");*/

        // 5) WebSocket 발사 (프론트의 /topic/notifications/{receiverId} 로 감)
        notificationService.sendRealTimeNotification(noti);
    }

    @Transactional(rollbackFor = Exception.class)
    public void bookingReject(BookingVO bookingVO) {
        // parmeterkey받아오기
        BookingVO booking = bookingDAO.showBookingOneByPaymentKey(bookingVO);

        String res = paymentService.paymentCancel(booking.getPaymentKey(), booking.getTotalFee(), 0, "임대인 거절");

        ObjectMapper om = new ObjectMapper();
        PaymentInfoDTO paymentInfoDTO = new PaymentInfoDTO();
        try {
            paymentInfoDTO = om.readValue(res, PaymentInfoDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        if (paymentInfoDTO.getCode() != null) throw new RuntimeException();
        System.out.println(paymentInfoDTO);

        // booking 테이블 허가 수정
        bookingVO.setRentApproval(false);
        bookingDAO.bookingReject(bookingVO);

        // 물품 배정 재정의 및 트럭 대여 반납 처리 (프로젝트 반려로 메소드 추가하기)
        if(booking.isUseService()) {
            projectService.projectReject(booking); // 프로젝트 반려 추가
            ItemServiceDTO itemServiceDTO = null;
            itemServiceDTO = serviceManagementService.getIsIdByBookingId(booking.getBookingId());
            if(itemServiceDTO != null) {
                itemService.setIsIdToZero(itemServiceDTO.getIsId());
            }


            if(itemServiceDTO !=null && itemServiceDTO.isTransport()) {
                transportServiceService.setTsReturned(itemServiceDTO.getTsId());
            }
        }

        // PaymentInfo 데이터 수정
        paymentInfoService.paymentInfoCancelUpdate(paymentInfoDTO);

        // cancel_info 삽입
        for (CancelINfoDTO c : paymentInfoDTO.getCancels()) {
            if (c.getTransactionKey().equals(paymentInfoDTO.getLastTransactionKey())) {
                CancelInfoVO cancelInfoVO = CancelInfoVO.builder()
                        .transactionKey(paymentInfoDTO.getLastTransactionKey())
                        .cancelReason(c.getCancelReason())
                        .cancelStatus(c.getCancelStatus())
                        .cancelAmount(c.getCancelAmount())
                        .canceledAt(c.getCanceledAt())
                        .paymentKey(paymentInfoDTO.getPaymentKey())
                        .userId(bookingVO.getUserId())
                        .build();
                cancelInfoService.cancelInfoInsert(cancelInfoVO);

                // 6) 임차인에게 "반려+결제취소" 알림 소켓 발사
                Integer tenantId  = bookingVO.getUserId();                      // 프론트에서 넘어온 userId
                //Integer bookingId = bookingVO.getBookingId();                   // 또는 bookingVOHasPaymentKey.getBookingId()

                //String message = "예약이 반려되었습니다. 예약번호: " + bookingId;

                NotificationDTO noti = new NotificationDTO();
                noti.setReceiverId(tenantId);
                /*noti.setBookingId(bookingId);
                noti.setMessage(message);
                noti.setIsRead("N");*/

                notificationService.sendRealTimeNotification(noti);
            }
        }


    }

    public List<MyReservationDTO> getMyBookings(int userId) {
        return bookingDAO.getMyBookings(userId);
    }

    public List<BookingVO> showBookingList(BookingVO vo) {
        return bookingDAO.showBookingList(vo);
    }


    public BookingDetailDTO getBookingDetail(int bookingId, int userId) {
        BookingDetailDTO bookingDetail = bookingDAO.getBookingDetail(bookingId, userId);

        if(bookingDetail.isTransport()) {
            bookingDetail.setTransportInfo(bookingDAO.getBookingTransportInfo(bookingId));
        }
        if(bookingDetail.isItems()) {
            bookingDetail.setItemInfoList(bookingDAO.getBookingItemInfoList(bookingId));
        }

        return bookingDetail;
    }

    @Transactional(rollbackFor = Exception.class)
    public void processCancellation(int bookingId, int userId) throws CustomCancellationException {
        BookingVO booking = bookingDAO.findBookingById(bookingId, userId)
                .orElseThrow(() -> new CustomCancellationException("존재하지 않는 예약입니다"));

        if(booking.isCanceled()) {
            throw new CustomCancellationException("이미 취소된 예약입니다.");
        }

        String paymentKey = booking.getPaymentKey();
        final Integer totalFee = booking.getTotalFee();
        final LocalDate bookingStartDate = booking.getBookingStart();
        final LocalDate today = LocalDate.now();

        long daysUntilStart = ChronoUnit.DAYS.between(today, bookingStartDate);

        Integer penaltyFee;

        if (daysUntilStart >= 7) {
            penaltyFee = 0; // 7일 전: 수수료 없음
        } else if (daysUntilStart >= 3) {
            penaltyFee = (int) (totalFee * 0.1); // 3~6일 전: 10% 위약금
        } else if (daysUntilStart >= 1) {
            penaltyFee = (int) (totalFee * 0.5); // 1~2일 전: 50% 위약금
        } else {
            penaltyFee = totalFee; // 당일 취소: 전액 환불 불가 (100% 위약금)
        }

        Integer actualRefundAmount = totalFee - penaltyFee;
        String cancelReason = "임차인 취소 요청 (위약금: " + penaltyFee + "원)";

        String res = "";

        if(actualRefundAmount <= 0) {
            System.out.println("DEBUG: 환불 금액이 0원 입니다. 토스 API 호출을 건너뜁니다.");
        }else {
            res = paymentService.paymentCancel(paymentKey, totalFee, penaltyFee, "예약 취소 (위약금 " + penaltyFee + "원 차감)");

            // 4. Toss 응답 JSON 파싱
            ObjectMapper om = new ObjectMapper();
            PaymentInfoDTO paymentInfoDTO = new PaymentInfoDTO();
            try {
                paymentInfoDTO = om.readValue(res, PaymentInfoDTO.class);
            } catch (JsonProcessingException e) {
                // JSON 파싱 오류는 치명적이므로 RuntimeException 처리
                throw new RuntimeException("토스 응답 JSON 파싱 실패", e);
            }

            // 5. Toss API 오류 코드 확인
            if (paymentInfoDTO.getCode() != null) {
                // API 오류 발생 시, 해당 결제 건은 취소 처리를 중단하고 롤백 (트랜잭션이 중요함)
                String errorMessage = "토스 결제 취소 API 오류 [" + paymentInfoDTO.getCode() + "]: " + paymentInfoDTO.getMessage();
                throw new RuntimeException(errorMessage);
            }

            System.out.println(paymentInfoDTO);

            // 물품 배정 재정의 및 트럭 대여 반납 처리
            if(booking.isUseService()) {
                projectService.projectReject(booking); // 프로젝트 반려 추가
                System.out.println("======반납======");
                System.out.println(booking);
                ItemServiceDTO itemServiceDTO = null;
                itemServiceDTO = serviceManagementService.getIsIdByBookingId(booking.getBookingId());
                if(itemServiceDTO != null) {
                    itemService.setIsIdToZero(itemServiceDTO.getIsId());
                    System.out.println("물건반납");
                }

                if(itemServiceDTO !=null && itemServiceDTO.isTransport()) {
                    transportServiceService.setTsReturned(itemServiceDTO.getTsId());
                    System.out.println("트럭반납");
                }
            }

            // 6. PaymentInfo 데이터 수정 및 CancelInfo 삽입
            paymentInfoService.paymentInfoCancelUpdate(paymentInfoDTO);

            // cancel_info 삽입
            for (CancelINfoDTO c : paymentInfoDTO.getCancels()) {
                if (c.getTransactionKey().equals(paymentInfoDTO.getLastTransactionKey())) {
                    CancelInfoVO cancelInfoVO = CancelInfoVO.builder()
                            .transactionKey(paymentInfoDTO.getLastTransactionKey())
                            .cancelReason(c.getCancelReason())
                            .cancelStatus(c.getCancelStatus())
                            .cancelAmount(c.getCancelAmount())
                            .canceledAt(c.getCanceledAt())
                            .paymentKey(paymentInfoDTO.getPaymentKey())
                            .userId(booking.getUserId()) // 현재 취소를 요청한 임차인 ID
                            .build();
                    cancelInfoService.cancelInfoInsert(cancelInfoVO);
                }
            }
        }


        bookingDAO.updateBookingAsCanceled(bookingId, penaltyFee, actualRefundAmount);

    }

    // 완료된 예약 리스트업 (리뷰)
    public List<BookingDTO> getCompletedBookings(int userId) {
        return bookingDAO.getCompletedBookings(userId);
    }
}
