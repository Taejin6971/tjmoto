package com.mysite.tjmoto.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mysite.tjmoto.dto.MainItemDto;

public interface ItemRepositoryCustom {

	Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

	Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}