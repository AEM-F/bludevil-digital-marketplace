package nl.fhict.digitalmarketplace.service.chat;

import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.chat.ChatMessage;
import nl.fhict.digitalmarketplace.model.chat.MessageStatus;
import nl.fhict.digitalmarketplace.model.user.User;
import nl.fhict.digitalmarketplace.repository.chat.ChatMessageRepository;
import nl.fhict.digitalmarketplace.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMessageService {
    private UserRepository userRepository;
    private ChatMessageRepository chatMessageRepository;
    private ChatRoomService chatRoomService;

    public ChatMessageService(UserRepository userRepository, ChatMessageRepository chatMessageRepository, ChatRoomService chatRoomService) {
        this.userRepository = userRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomService = chatRoomService;
    }

    public ChatMessage save(ChatMessage chatMessage){
        chatMessage.setStatus(MessageStatus.RECEIVED);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    public long countNewMessages(Integer senderId, Integer recipientId) throws ResourceNotFoundException {
        User recipient = userRepository.getById(recipientId);
        User sender = userRepository.getById(senderId);
        if(recipient != null && sender != null){
            return chatMessageRepository.countBySenderIdAndRecipientIdAndStatus(senderId, recipientId, MessageStatus.RECEIVED);
        }else{
            throw new ResourceNotFoundException("Users not found");
        }
    }

    public long countAllNewMessages(Integer recipientId) throws ResourceNotFoundException {
        User returnedUser = userRepository.getById(recipientId);
        if(returnedUser != null){
            return chatMessageRepository.countByRecipientIdAndStatus(recipientId, MessageStatus.RECEIVED);
        }else{
         throw new ResourceNotFoundException("No recipient found");
        }
    }

    public List<ChatMessage> findChatMessages(Integer senderId, Integer recipientId){
        String chatId = chatRoomService.getChatId(senderId, recipientId, false);
        List<ChatMessage> messages = chatMessageRepository.findAllByChatId(chatId);
        if(messages.size() > 0){
            updateStatuses(senderId, recipientId, MessageStatus.DELIVERED);
        }
        return messages;
    }

    public void updateStatuses(Integer senderId, Integer recipientId, MessageStatus messageStatus){
        List<ChatMessage> messages = chatMessageRepository.findAllBySenderIdAndRecipientId(senderId, recipientId).stream().map(message ->{
            message.setStatus(messageStatus);
            return message;
        }).collect(Collectors.toList());
        chatMessageRepository.saveAll(messages);
    }


    public ChatMessage findById(Integer id) throws ResourceNotFoundException {
        ChatMessage foundMessage = chatMessageRepository.getByMessageId(id);
        if(foundMessage != null){
            foundMessage.setStatus(MessageStatus.DELIVERED);
            return chatMessageRepository.save(foundMessage);
        }else {
            throw new ResourceNotFoundException("Chat message not found");
        }
    }
}
