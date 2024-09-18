package com.sjy.milestone.cart.controller;

import com.sjy.milestone.cart.CartService;
import com.sjy.milestone.session.SesssionConst;
import com.sjy.milestone.cart.dto.CartItemDTO;
import com.sjy.milestone.cart.dto.CartItemRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addCartItem(@RequestBody CartItemRequestDTO cartItemRequestDTO) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.addCartItem(userEmail, cartItemRequestDTO);
        return ResponseEntity.ok("장바구니에 추가되었습니다");
    }

    @GetMapping
    public ResponseEntity<List<CartItemDTO>> getCartItems() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(cartService.getCartItems(userEmail));
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<String> removeFromCart (@PathVariable Long cartItemId) {
        cartService.removeFromCart(cartItemId);
        return ResponseEntity.noContent().build();
    }
}