package com.java.erp.pm.service;

import com.java.erp.pm.dao.TransportDAO;
import com.java.erp.pm.model.vo.TransportVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransportService {

    final TransportDAO transportDAO;

    public List<TransportVO> transportSelect() {
        return transportDAO.transportSelect();
    }

    public TransportVO transportOneSelect(int transportId) {
        return transportDAO.transportOneSelect(transportId);
    }

    public void addTransport(int transportId, int qty) {
        transportDAO.addTransport(transportId, qty);
    }

    public void addNewTransport(int truckNum, int truckFee) {
        transportDAO.addNewTransport(truckNum, truckFee);
    }
}
