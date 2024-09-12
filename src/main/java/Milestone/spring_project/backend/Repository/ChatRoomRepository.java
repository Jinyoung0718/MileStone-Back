package Milestone.spring_project.backend.Repository;

import Milestone.spring_project.backend.domain.Entity.Auth.Member;
import Milestone.spring_project.backend.domain.Entity.Chat.ChatRoom;
import Milestone.spring_project.backend.domain.Entity.Chat.ChatRoomStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    @NotNull
    Optional<ChatRoom> findById(@NotNull String roomId);
    List<ChatRoom> findByUserAndStatus(Member user, ChatRoomStatus status);

    Optional<ChatRoom> findByIdAndStatus(String roomId, ChatRoomStatus chatRoomStatus);
}
