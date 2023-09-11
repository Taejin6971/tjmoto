package com.mysite.tjmoto.cart;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CartController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/cart")
    public String orderHist(){
    	
        return "cart/cartList";
    }

}