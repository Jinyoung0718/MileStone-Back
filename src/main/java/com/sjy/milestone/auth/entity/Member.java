package com.sjy.milestone.auth.entity;

import com.sjy.milestone.auth.MemberStatus;
import com.sjy.milestone.cart.entity.CartItem;
import com.sjy.milestone.chat.entity.ChatRoom;
import com.sjy.milestone.board.entity.Board;
import com.sjy.milestone.comment.entity.Comment;
import com.sjy.milestone.memberaddress.entity.MemberAddress;
import com.sjy.milestone.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEMBERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "MemberStatus == activate")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USER_EMAIL", nullable = false)
    private String userEmail;

    @Column(name = "USER_PASSWORD", nullable = false)
    private String userPassword;

    @Column(name = "USER_NAME",length = 100)
    private String userName;

    @CreationTimestamp // 저장 시 현재 시간으로 해당 필드를 저장 시킨다.
    @Column(name = "REGISTRATION_DATE", updatable = false)
    private LocalDateTime registrationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 11)
    private MemberStatus status = MemberStatus.ACTIVATE;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> chatRoomsAsUser = new ArrayList<>();

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> chatRoomsAsAdmin = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MemberAddress> memberAddresses = new ArrayList<>();

    @Builder
    protected Member(String userEmail, String userPassword, String userName, MemberStatus status) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userName = userName;
        this.status = status;
    }

    public void deactivate() {
        this.status = MemberStatus.DEACTIVATED;
    }

    public void reactivate() {
        this.status = MemberStatus.ACTIVATE;
    }
}