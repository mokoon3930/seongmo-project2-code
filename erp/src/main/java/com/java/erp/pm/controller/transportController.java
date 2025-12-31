package com.java.erp.pm.controller;

import com.java.erp.pm.model.vo.TransportVO;

import com.java.erp.pm.service.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class transportController {

    final TransportService transportService;

    @GetMapping("/transport")
    public String transportSelect(Model model){
        List<TransportVO> vo = transportService.transportSelect();
                model.addAttribute("vo", vo);
        return "transport/transportSelect";
    }

    @GetMapping("/addTransport")
    public String transportOneSelect(int transportId, Model model){
        TransportVO vo = transportService.transportOneSelect(transportId);
        model.addAttribute("vo", vo);
        return "transport/addTransport";
    }

    @PostMapping("/addTransport")
    public String addTransport(int transportId, int qty){

        transportService.addTransport(transportId, qty);

        return "redirect:/transport";
    }

    @GetMapping("/addNewTransport")
    public String addNewTransport(){
        return "transport/addNewTransport";
    }

    @PostMapping("addNewTransport")
    public String addNewTransport(int truckNum, int truckFee){
        transportService.addNewTransport(truckNum, truckFee);
        return "redirect:/transport";
    }

}
