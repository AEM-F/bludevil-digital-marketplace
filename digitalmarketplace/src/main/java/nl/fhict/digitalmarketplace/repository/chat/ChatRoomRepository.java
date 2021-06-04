package nl.fhict.digitalmarketplace.repository.chat;

import nl.fhict.digitalmarketplace.model.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    ChatRoom findBySenderIdAndRecipientId(Integer senderId, Integer recipientId);
    List<ChatRoom> findAllByChatId(String chatId);
}
