package com.java.erp.pm.dao;

import com.java.erp.pm.model.dto.CreatedItemDTO;
import com.java.erp.pm.model.dto.PmDTO;
import com.java.erp.pm.model.vo.ItemCategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ItemDAO {

    // 아이템 카테고리 조회
    List<ItemCategoryVO> selectAll();

    // 아이템 세부정보
    List<PmDTO> selectItem(Integer icId);

    // 아이템 구메 페이지
    PmDTO itemAdd(Integer itemInfoId);

    // 아이템 구메 post
    Integer postItemAdd(int itemInfoId);

    // 카테고리 등록
    int createdCategory(ItemCategoryVO vo);

    // 새 아이템 생성
    int createdItem(CreatedItemDTO dto);
}
