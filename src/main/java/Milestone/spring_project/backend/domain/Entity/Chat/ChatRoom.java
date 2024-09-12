package Milestone.spring_project.backend.domain.Entity.Chat;

import Milestone.spring_project.backend.domain.Entity.Auth.Member;
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
@Table(name = "CHAT_ROOMS")
@Getter @NoArgsConstructor
public class ChatRoom {

    @Id @Column(name = "CHAT_ROOM_ID", length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private Member user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADMIN_ID")
    private Member admin;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false) @Setter
    private ChatRoomStatus status = ChatRoomStatus.PENDING;

    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>();

    @Builder
    public ChatRoom(String id, Member user, Member admin, ChatRoomStatus status) {
        this.id = id;
        this.user = user;
        this.admin = admin;
        this.status = status;
    }

    public static ChatRoom acceptCreateActiveRoom(String id, Member user, Member admin) {
        return ChatRoom.builder()
                .id(id)
                .user(user)
                .admin(admin)
                .status(ChatRoomStatus.ACTIVE)
                .build();
    }
}
