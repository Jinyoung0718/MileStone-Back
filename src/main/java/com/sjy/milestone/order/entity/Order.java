package com.sjy.milestone.order.entity;

import com.sjy.milestone.account.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDERS")
@Getter @NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(name = "MERCHAT_UID", nullable = false, unique = true, length = 50)
    private String merchantUid;

    @Column(name = "IMP_UID", length = 50) @Setter
    private String impUid;

    @Column(name = "RECIPIENT_NAME", nullable = false, length = 100)
    private String recipientName;

    @Column(name = "DELIVERY_ADDRESS", nullable = false)
    private String deliveryAddress;

    @Column(name = "DELIVERY_DETAIL", nullable = false)
    private String deliveryDetail;

    @Column(name = "DELIVERY_ZIPCODE", nullable = false, length = 10)
    private String deliveryZipcode;

    @Column(name = "PHONE_NUMBER", nullable = false, length = 11)
    private String phoneNumber;

    @CreationTimestamp
    @Column(name = "ORDER_DATE", nullable = false, updatable = false)
    private LocalDateTime orderDate;

    @Column(name = "STATUS", nullable = false, length = 10)
    @Enumerated(EnumType.STRING) @Setter
    private OrderStatus status;

    @Column(name = "TOTAL_PRICE", nullable = false)
    private Long totalPrice;

    @Column(name = "REQUEST_MESSAGE", columnDefinition = "TEXT")
    private String  requestMessage;

    @Builder
    public Order(Member member, String merchantUid, OrderStatus status, Long totalPrice, String requestMessage,
                 String recipientName, String deliveryAddress, String deliveryDetail, String deliveryZipcode, String phoneNumber) {
        this.member = member;
        this.merchantUid = merchantUid;
        this.status = status;
        this.totalPrice = totalPrice;
        this.requestMessage = requestMessage;
        this.recipientName = recipientName;
        this.deliveryAddress = deliveryAddress;
        this.deliveryDetail = deliveryDetail;
        this.deliveryZipcode = deliveryZipcode;
        this.phoneNumber = phoneNumber;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
}