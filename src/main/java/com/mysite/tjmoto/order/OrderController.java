package com.mysite.tjmoto.order;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

	@GetMapping(value = { "/orders", "/orders/{page}" })
	public String orderHist() {

		return "order/orderHist";
	}

}