package com.sjy.milestone.order.entity;

import com.sjy.milestone.order.dto.TempOrderDTO;
import com.sjy.milestone.order.dto.TempOrderItemDTO;
import com.sjy.milestone.auth.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TEMP_ORDERS")
@Getter @NoArgsConstructor
public class TempOrder {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEMP_ORDER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "tempOrder", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TempOrderItem> tempOrderItems = new ArrayList<>();

    @Column(name = "TOTAL_PRICE", nullable = false)
    protected Long totalPrice;

    @Builder
    public TempOrder(Member member, List<TempOrderItem> tempOrderItems, Long totalPrice) {
        this.member = member;
        this.totalPrice = totalPrice;
        if (tempOrderItems != null) {
            this.tempOrderItems = tempOrderItems;
            this.tempOrderItems.forEach(item -> item.setTempOrder(this));
        }
    }

    public void addTempOrderItem(TempOrderItem tempOrderItem) {
        tempOrderItems.add(tempOrderItem);
        tempOrderItem.setTempOrder(this);
    }

    public TempOrderDTO toDTO() {
        List<TempOrderItemDTO> tempOrderItemDTOS = tempOrderItems.stream()
                .map(TempOrderItem::toDTO)
                .toList();

        return TempOrderDTO.builder()
                .id(this.id)
                .tempOrderItems(tempOrderItemDTOS)
                .totalPrice(this.totalPrice)
                .build();
    }
}
