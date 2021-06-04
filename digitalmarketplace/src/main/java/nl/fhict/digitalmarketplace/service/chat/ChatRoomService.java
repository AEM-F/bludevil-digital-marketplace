package nl.fhict.digitalmarketplace.service.chat;

import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.chat.ChatMessage;
import nl.fhict.digitalmarketplace.model.chat.ChatRoom;
import nl.fhict.digitalmarketplace.repository.chat.ChatMessageRepository;
import nl.fhict.digitalmarketplace.repository.chat.ChatRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ChatRoomService {
    private ChatRoomRepository chatRoomRepository;
    private ChatMessageRepository chatMessageRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    public String getChatId(Integer senderId, Integer recipientId, boolean createIfNotExist){
        ChatRoom returnedChatRoom = chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId);
        if(returnedChatRoom != null){
            return returnedChatRoom.getChatId();
        }else{
            if(createIfNotExist){
                String chatId = String.format("%s_%s", senderId, recipientId);

                ChatRoom senderRecipient = new ChatRoom();
                senderRecipient.setChatId(chatId);
                senderRecipient.setSenderId(senderId);
                senderRecipient.setRecipientId(recipientId);

                ChatRoom recipientSender = new ChatRoom();
                recipientSender.setChatId(chatId);
                recipientSender.setSenderId(recipientId);
                recipientSender.setRecipientId(senderId);

                chatRoomRepository.save(senderRecipient);
                chatRoomRepository.save(recipientSender);

                return chatId;
            }
            else{
                return null;
            }
        }
    }

    public void deleteChat(String chatId) throws ResourceNotFoundException {
        List<ChatRoom> returnedChatRooms = chatRoomRepository.findAllByChatId(chatId);
        if(returnedChatRooms != null){
            List<ChatMessage> messages = chatMessageRepository.findAllByChatId(chatId);
            if(!messages.isEmpty()){
                chatMessageRepository.deleteAll(messages);
            }
            chatRoomRepository.deleteAll(returnedChatRooms);
        }
        else{
            throw new ResourceNotFoundException("The given chat room was not found");
        }
    }
}
