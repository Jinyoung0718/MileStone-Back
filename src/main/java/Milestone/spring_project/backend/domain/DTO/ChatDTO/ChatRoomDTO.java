package Milestone.spring_project.backend.domain.DTO.ChatDTO;

import Milestone.spring_project.backend.domain.Entity.Chat.ChatRoom;
import Milestone.spring_project.backend.domain.Entity.Chat.ChatRoomStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ChatRoomDTO {

    private String roomId;
    private String userEmail;
    private String adminEmail;
    private ChatRoomStatus status;
    private LocalDate createdAt;

    public static ChatRoomDTO fromEntity(ChatRoom chatRoom) {
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        chatRoomDTO.setRoomId(chatRoom.getId());
        chatRoomDTO.setUserEmail(chatRoom.getUser().getUserEmail());
        chatRoomDTO.setAdminEmail(chatRoom.getAdmin().getUserEmail());
        chatRoomDTO.setStatus(chatRoom.getStatus());
        chatRoomDTO.setCreatedAt(chatRoom.getCreatedAt().toLocalDate());
        return chatRoomDTO;
    }

    public static ChatRoomDTO fromRedis(String roomId, String userEmail, ChatRoomStatus status) {
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        chatRoomDTO.setRoomId(roomId);
        chatRoomDTO.setUserEmail(userEmail);
        chatRoomDTO.setStatus(status);
        chatRoomDTO.setCreatedAt(LocalDate.now());
        // Pending 일 경우의 목록을 불러들일 때 사용이 되므로 따로 admin 설정은 안 함, ACCEPT 할 때 admin 설정
        return chatRoomDTO;
    }

}