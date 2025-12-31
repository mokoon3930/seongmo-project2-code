package com.java.erp.pm.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransportVO {

    private int transportId;
    private int truckNum;
    private int truckFee;
    private int rented;
}
