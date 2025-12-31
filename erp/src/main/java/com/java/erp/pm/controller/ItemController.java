package com.java.erp.pm.controller;

import com.java.erp.pm.model.dto.CreatedItemDTO;
import com.java.erp.pm.model.dto.PmDTO;
import com.java.erp.pm.model.vo.ItemCategoryVO;
import com.java.erp.pm.model.vo.ItemInfoVO;
import com.java.erp.pm.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    // 아이템 카테고리 리스트
    @GetMapping("/item_category")
    public String item_category(Model model){
        List<ItemCategoryVO> vo = itemService.selectAll();
        model.addAttribute("vo", vo);
        return "/item/item_category";
    }

    // 아이템 세부정보
    @GetMapping("/category")
    public String itemDetail(Model model, Integer icId){
        List<PmDTO> dto = itemService.selectItem(icId);
        model.addAttribute("vo", dto);
        model.addAttribute("icId", icId);
        return "/item/item_detail";
    }

    // 아이템 구메 페이지
    @GetMapping("item_add")
    public String itemAdd(Integer itemInfoId, Model model){
        PmDTO dto = itemService.itemAdd(itemInfoId);
        model.addAttribute("vo", dto);
        return "/item/item_add";
    }

    // 아이템 구메 입력
    @PostMapping("item_add")
    public String postItemAdd(ItemInfoVO vo, int icId){
        itemService.postItemAdd(vo);
        return "redirect:/item/category?icId=" + icId;
    }

    //  새 카테고리 등록 페이지
    @GetMapping("create_category")
    public String createCategory(){
        return "/item/created_category";
    }

    // 새 카테고리 생성
    @PostMapping("created_category")
    public String createCategory(ItemCategoryVO vo){
        itemService.createdCategory(vo);
        return "redirect:/item/item_category";
    }

    // 새 아이템 생성 페이지
    @GetMapping("created_item")
    public String createdItem(Integer icId, Model model){
        model.addAttribute("icId", icId);
        return "/item/created_item";
    }

    // 새 아이템 생성 post
    @PostMapping("created_item")
    public String createdItem(CreatedItemDTO dto){
        itemService.createdItem(dto);
        return "redirect:/item/item_category";
    }
}
