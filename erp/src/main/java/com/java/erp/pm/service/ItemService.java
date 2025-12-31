package com.java.erp.pm.service;

import com.java.erp.pm.dao.ItemDAO;
import com.java.erp.pm.model.dto.CreatedItemDTO;
import com.java.erp.pm.model.dto.PmDTO;
import com.java.erp.pm.model.vo.ItemCategoryVO;
import com.java.erp.pm.model.vo.ItemInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemDAO itemcategoryDAO;

    // 아이템 카테고리 리스트
    public List<ItemCategoryVO> selectAll() {
        return itemcategoryDAO.selectAll();
    }

    // 아이템 세부정보
    public List<PmDTO> selectItem(Integer icId){
        return itemcategoryDAO.selectItem(icId);
    }

    // 아이템 구메 페이지
    public PmDTO itemAdd(Integer itemInfoId) {
        return itemcategoryDAO.itemAdd(itemInfoId);
    }

    // 아이템 구메 post
    public int postItemAdd(ItemInfoVO vo){

        int a = 0;

        for(int i = 0; i < vo.getQty(); i++) {
            itemcategoryDAO.postItemAdd(vo.getItemInfoId());
            a++;
        }
        return a;
    }

    // 새 카테고리 생성
    public int createdCategory(ItemCategoryVO vo) {
        return itemcategoryDAO.createdCategory(vo);
    }

    // 새 아이템 생성
    public int createdItem(CreatedItemDTO dto) {
        return itemcategoryDAO.createdItem(dto);
    }
}

