package nl.fhict.digitalmarketplace.repository.chat;

import nl.fhict.digitalmarketplace.model.chat.ChatMessage;
import nl.fhict.digitalmarketplace.model.chat.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    long countBySenderIdAndRecipientIdAndStatus(Integer senderId, Integer recipientId, MessageStatus status);
    List<ChatMessage> findAllByChatId(String chatId);
    List<ChatMessage> findAllBySenderIdAndRecipientId(Integer senderId, Integer recipientId);
    ChatMessage getByMessageId(Integer id);
    long countByRecipientIdAndStatus(Integer recipientId, MessageStatus status);
}
