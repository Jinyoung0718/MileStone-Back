package Milestone.spring_project.backend.Controller.Order;

import Milestone.spring_project.backend.Service.OrderService.OrderManagementService;
import Milestone.spring_project.backend.Service.OrderService.OrderService;
import Milestone.spring_project.backend.Util.Sesssion.SessionManager;
import Milestone.spring_project.backend.Util.Sesssion.SesssionConst;
import Milestone.spring_project.backend.Util.Validator.ValidatorService;
import Milestone.spring_project.backend.domain.DTO.OrderDTO.CompleteOrderDTO;
import Milestone.spring_project.backend.domain.DTO.OrderDTO.DirectOrderDTO;
import Milestone.spring_project.backend.domain.DTO.OrderDTO.OrderDTO;
import Milestone.spring_project.backend.domain.DTO.OrderDTO.TempOrderDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final SessionManager sessionManager;
    private final ValidatorService validatorService;
    private final OrderManagementService orderManagementService;

    // 장바구니의 상품들을 주문 페이지로 넘기는 엔드포인트
    @PostMapping("/fromCart")
    public ResponseEntity<TempOrderDTO> getOrderPageFromCart(@CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId,
                                                             @RequestBody List<Long> cartItemIds) {
        String userEmail = sessionManager.getSession(sessionId);
        TempOrderDTO tempOrderDTO = orderService.getCartItemsByIds(userEmail, cartItemIds);
        return ResponseEntity.ok(tempOrderDTO);
    }

    // 상품 상세 페이지에서 선택한 상품을 주문 페이지로 넘기는 엔드포인트
    @PostMapping("/direct")
    public ResponseEntity<TempOrderDTO> getDirectOrderPage(@CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId,
                                                           @RequestBody DirectOrderDTO directOrderDTO) {
        String userEmail = sessionManager.getSession(sessionId);
        TempOrderDTO tempOrderDTO = orderService.getDirectOrderItem(userEmail, directOrderDTO);
        return ResponseEntity.ok(tempOrderDTO);
    }

    // 주문 페이지에서 최종적으로 주문하는 엔드포인트
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId,
                                             @Valid @RequestBody TempOrderDTO tempOrderDTO, BindingResult bindingResult) {
        validatorService.validate(bindingResult);
        String userEmail = sessionManager.getSession(sessionId);
        String merchantUid = orderService.placeOrder(userEmail, tempOrderDTO);
        return ResponseEntity.ok(merchantUid);
    }

    @PostMapping("/complete")
    public ResponseEntity<String> completeOrder(@RequestBody CompleteOrderDTO completeOrderDTO) {
        orderService.completeOrder(completeOrderDTO.getImpUid(), completeOrderDTO.getMerchantUid());
        return ResponseEntity.ok("주문이 완료되었습니다");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelOrder(@CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId,
                                              @RequestParam Long orderId) {
        String userEmail = sessionManager.getSession(sessionId);
        orderService.cancelOrder(userEmail, orderId);
        return ResponseEntity.ok("주문이 취소되었습니다");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteOrder(@CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId,
                                              @RequestParam Long orderId) {
        String userEmail = sessionManager.getSession(sessionId);
        orderManagementService.deleteOrder(userEmail, orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orderList")
    public ResponseEntity<List<OrderDTO>> getOrderList(@CookieValue(value = SesssionConst.SESSION_COOKIE_NAME) String sessionId) {
        String userEmail = sessionManager.getSession(sessionId);
        List<OrderDTO> orders = orderService.getOrdersByUserEmail(userEmail);
        return ResponseEntity.ok(orders);
    }
}