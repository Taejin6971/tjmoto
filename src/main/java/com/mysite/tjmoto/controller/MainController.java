package com.mysite.tjmoto.controller;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mysite.tjmoto.dto.MainItemDto;
import com.mysite.tjmoto.item.ItemSearchDto;
import com.mysite.tjmoto.item.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

	private final ItemService itemService;

	@GetMapping(value = "/")
	public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {

		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);

		Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

		model.addAttribute("items", items);

		return "main";
	}

	@GetMapping(value = "/helmet")
	public String helmet(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model) {

		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 12);

		Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

		model.addAttribute("items", items);
		model.addAttribute("itemSearchDto", itemSearchDto);
		model.addAttribute("maxPage", 5);

		return "helmet";
	}

}