package com.java.erp.pm.dao;

import com.java.erp.pm.model.vo.TransportVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransportDAO {
    List<TransportVO> transportSelect();

    TransportVO transportOneSelect(int transportId);

    int addTransport(int transportId, int qty);

    void addNewTransport(int truckNum, int truckFee);
}
