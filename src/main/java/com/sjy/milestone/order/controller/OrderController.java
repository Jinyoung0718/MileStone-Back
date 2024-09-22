package com.sjy.milestone.order.controller;

import com.sjy.milestone.order.service.OrderService;
import com.sjy.milestone.validator.ValidatorService;
import com.sjy.milestone.order.dto.order.CompleteOrderDTO;
import com.sjy.milestone.order.dto.order.DirectOrderDTO;
import com.sjy.milestone.order.dto.order.OrderDTO;
import com.sjy.milestone.order.dto.temp.TempOrderDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ValidatorService validatorService;

    // 장바구니의 상품들을 주문 페이지로 넘기는 엔드포인트
    @PostMapping("/fromCart")
    public ResponseEntity<TempOrderDTO> getOrderPageFromCart(@RequestBody List<Long> cartItemIds) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        TempOrderDTO tempOrderDTO = orderService.getCartItemsByIds(userEmail, cartItemIds);
        return ResponseEntity.ok(tempOrderDTO);
    }

    // 상품 상세 페이지에서 선택한 상품을 주문 페이지로 넘기는 엔드포인트
    @PostMapping("/direct")
    public ResponseEntity<TempOrderDTO> getDirectOrderPage(@RequestBody DirectOrderDTO directOrderDTO) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        TempOrderDTO tempOrderDTO = orderService.getDirectOrderItem(userEmail, directOrderDTO);
        return ResponseEntity.ok(tempOrderDTO);
    }

    // 주문 페이지에서 최종적으로 주문하는 엔드포인트
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody TempOrderDTO tempOrderDTO, BindingResult bindingResult) {
        validatorService.validate(bindingResult);
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String merchantUid = orderService.placeOrder(userEmail, tempOrderDTO);
        return ResponseEntity.ok(merchantUid);
    }

    @PostMapping("/complete")
    public ResponseEntity<String> completeOrder(@RequestBody CompleteOrderDTO completeOrderDTO) {
        orderService.completeOrder(completeOrderDTO.getImpUid(), completeOrderDTO.getMerchantUid());
        return ResponseEntity.ok("주문이 완료되었습니다");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelOrder(@RequestParam Long orderId) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        orderService.cancelOrder(userEmail, orderId);
        return ResponseEntity.ok("주문이 취소되었습니다");
    }

    @GetMapping("/orderList")
    public ResponseEntity<List<OrderDTO>> getOrderList() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<OrderDTO> orders = orderService.getOrdersByUserEmail(userEmail);
        return ResponseEntity.ok(orders);
    }
}