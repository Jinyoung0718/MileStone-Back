package com.sjy.milestone.cart;

import com.sjy.milestone.cart.repository.CartItemRepository;
import com.sjy.milestone.account.repository.MemberRepository;
import com.sjy.milestone.exception.badrequest.CartItemAlreadyExistsException;
import com.sjy.milestone.exception.badrequest.InsufficientStockException;
import com.sjy.milestone.exception.notfound.CartItemNotFoundException;
import com.sjy.milestone.exception.notfound.ProductOptionNotFoundException;
import com.sjy.milestone.product.repository.ProductOptionRepository;
import com.sjy.milestone.account.validator.MemberValidator;
import com.sjy.milestone.cart.dto.CartItemDTO;
import com.sjy.milestone.cart.dto.CartItemRequestDTO;
import com.sjy.milestone.account.entity.Member;
import com.sjy.milestone.cart.entity.CartItem;
import com.sjy.milestone.product.entity.ProductOption;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ProductOptionRepository productOptionRepository;
    private final MemberValidator memberValidator;

    public void addCartItem(String userEmail, CartItemRequestDTO cartItemRequestDTO) {
        Member member = memberRepository.findByUserEmail(userEmail);
        memberValidator.validateMember(member);

        ProductOption productOption = productOptionRepository.findProductOptionWithProductById(cartItemRequestDTO.getProductOptionId())
                .orElseThrow(() -> new ProductOptionNotFoundException("상품 옵션 정보를 찾을 수 없습니다."));

        if (productOption.getStockQuantity() < cartItemRequestDTO.getQuantity()) {
            throw new InsufficientStockException("재고가 부족합니다.");
        }

        Optional<CartItem> existingCartItem = cartItemRepository.findByMemberAndProductOption(member, productOption);

        if (existingCartItem.isPresent()) {
            throw new CartItemAlreadyExistsException("이미 장바구니에 있는 상품입니다");
        }

        CartItem cartItem = cartItemRequestDTO.toEntity(member ,productOption);
        cartItemRepository.save(cartItem);
    }

    @Transactional(readOnly = true)
    public List<CartItemDTO> getCartItems(String userEmail) {
        Member member = memberRepository.findMemberWithOrdersByUserEmail(userEmail);
        memberValidator.validateMember(member);

        List<CartItem> cartItems = cartItemRepository.findByMember(member);

        return cartItems.stream()
                .map(CartItem::toDTO)
                .toList();
    }

    public void removeFromCart(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartItemNotFoundException("장바구니 항목을 찾을 수 없습니다"));
        cartItemRepository.delete(cartItem);
    }
}

