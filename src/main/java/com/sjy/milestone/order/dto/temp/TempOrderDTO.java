package com.sjy.milestone.order.dto.temp;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TempOrderDTO {

    private Long id;

    @NotNull(message = "총 가격은 필수 항목입니다.")
    private Long totalPrice;

    @NotEmpty(message = "주문 항목 리스트는 비어 있을 수 없습니다.")
    private List<TempOrderItemDTO> tempOrderItems;

    @NotEmpty(message = "요청 메시지를 입력해주세요.")
    private String requestMessage;

    @NotEmpty(message = "수령인 이름은 필수 항목입니다.")
    private String recipientName;

    @NotEmpty(message = "주소는 필수 항목입니다.")
    private String deliveryAddress;

    @NotEmpty(message = "상세 주소는 필수 항목입니다.")
    private String deliveryDetail;

    @Column(name = "DELIVERY_ZIPCODE", nullable = false)
    private String deliveryZipcode;

    @NotEmpty(message = "전화번호는 필수 항목입니다.")
    private String phoneNumber;

    public static TempOrderDTO of(List<TempOrderItemDTO> tempOrderItems, Long totalPrice) {
        return TempOrderDTO.builder()
                .tempOrderItems(tempOrderItems)
                .totalPrice(totalPrice)
                .build();
    }
}