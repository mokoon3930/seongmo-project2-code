package com.java.ecommerce.crm.dao;

import com.java.ecommerce.crm.model.dto.*;
import com.java.ecommerce.crm.model.vo.BookingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BookingDAO {
    void bookingRegister(BookingVO bookingVO);
    List<BookingInfoDTO> showBookingInfo(BookingVO bookingVO);
    void bookingApprove(BookingVO vo);

    List<MyReservationDTO> getMyBookings(int userId);

    Optional<BookingVO> findBookingById(@Param("bookingId") int bookingId, @Param("userId") int userId);

    void bookingReject(BookingVO vo);
    BookingVO showBookingOneByPaymentKey(BookingVO vo);
    List<BookingVO> showBookingList(BookingVO vo);

    BookingDTO findUserIdByRrId(int rrId);

    BookingDetailDTO getBookingDetail(@Param("bookingId") int bookingId, @Param("userId") int userId);

    BookingTransportInfoDto getBookingTransportInfo(int bookingId);

    List<BookingItemInfoDto> getBookingItemInfoList(int bookingId);

    int updateBookingAsCanceled(@Param("bookingId") int bookingId, @Param("penaltyFee") int penaltyFee, @Param("refundAmount") int refundAmount);

    //완료된 booking 목록 리스트업 (리뷰)
    List<BookingDTO> getCompletedBookings(@Param("userId") int userId);
}
