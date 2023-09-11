package com.mysite.tjmoto.item;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

	// select * from item_img where item_id = ? order by id asc
	List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

	// select * from item_img where item_id = ? and repimg_yn = ?
	ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);

}