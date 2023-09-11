package com.mysite.tjmoto.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mysite.tjmoto.dto.MainItemDto;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	private final ItemImgService itemImgService;

	private final ItemImgRepository itemImgRepository;

	public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

		// 상품 등록
		Item item = itemFormDto.createItem();
		itemRepository.save(item);

		// 이미지 등록
		for (int i = 0; i < itemImgFileList.size(); i++) {

			ItemImg itemImg = new ItemImg();

			itemImg.setItem(item);

			if (i == 0)
				itemImg.setRepimgYn("Y");
			else
				itemImg.setRepimgYn("N");

			itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
		}

		return item.getId();
	}

	@Transactional(readOnly = true)
	public ItemFormDto getItemDtl(Long itemId) {

		List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

		List<ItemImgDto> itemImgDtoList = new ArrayList<>();

		for (ItemImg itemImg : itemImgList) {

			ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
			itemImgDtoList.add(itemImgDto);
		}

		Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new);

		ItemFormDto itemFormDto = ItemFormDto.of(item);
		itemFormDto.setItemImgDtoList(itemImgDtoList);
		return itemFormDto;
	}

	public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
		// 상품 수정
		Item item = itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new);

		item.updateItem(itemFormDto);
		List<Long> itemImgIds = itemFormDto.getItemImgIds();

		// 이미지 등록
		for (int i = 0; i < itemImgFileList.size(); i++) {
			itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
		}

		return item.getId();
	}

	@Transactional(readOnly = true)
	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		return itemRepository.getAdminItemPage(itemSearchDto, pageable);
	}

	@Transactional(readOnly = true)
	public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		return itemRepository.getMainItemPage(itemSearchDto, pageable);
	}
	
	public Item getItem(Long id) {
		
		Optional<Item> item = itemRepository.findById(id);
		
		if (item.isPresent()) {
			return item.get();
		} else {
			return null;
		}
	}

	public void delete(Item item) {
		itemRepository.delete(item);
	}

}