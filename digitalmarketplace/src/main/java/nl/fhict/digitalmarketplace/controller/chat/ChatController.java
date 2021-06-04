package nl.fhict.digitalmarketplace.controller.chat;

import nl.fhict.digitalmarketplace.customException.InvalidInputException;
import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.chat.ChatMessage;
import nl.fhict.digitalmarketplace.model.chat.ChatNotification;
import nl.fhict.digitalmarketplace.model.response.ContactResponse;
import nl.fhict.digitalmarketplace.model.response.PaginationResponse;
import nl.fhict.digitalmarketplace.model.user.ERole;
import nl.fhict.digitalmarketplace.service.chat.ChatMessageService;
import nl.fhict.digitalmarketplace.service.chat.ChatRoomService;
import nl.fhict.digitalmarketplace.service.user.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
public class ChatController {
    private SimpMessagingTemplate messagingTemplate;
    private ChatMessageService chatMessageService;
    private ChatRoomService chatRoomService;
    private IUserService userService;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService, ChatRoomService chatRoomService, IUserService userService) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
        this.userService = userService;
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage){
        // createIfNotExist was true
        String chatId = chatRoomService.getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        if(chatId != null){
            chatMessage.setChatId(chatId);
            ChatMessage savedMessage = chatMessageService.save(chatMessage);
            messagingTemplate.convertAndSendToUser(
                    chatMessage.getRecipientId().toString(),
                    "/queue/messages",
                    new ChatNotification(savedMessage.getMessageId(), savedMessage.getSenderId(), savedMessage.getSenderName()));
        }
    }

//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("api/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Object> countNewMessages(@PathVariable(name ="senderId") Integer senderId,
                                                   @PathVariable(name ="recipientId") Integer recipientId) throws ResourceNotFoundException {
        HashMap<String, Long> responseBody = new HashMap<>();
        responseBody.put("newMessagesNr", chatMessageService.countNewMessages(senderId, recipientId));
        return ResponseEntity.ok(responseBody);
    }

    //    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("api/messages/{recipientId}/count")
    public ResponseEntity<Object> countAllNewMessages(@PathVariable(name = "recipientId") int recipientId) throws ResourceNotFoundException {
        HashMap<String, Long> responseBody = new HashMap<>();
        responseBody.put("allNewMessagesNr", chatMessageService.countAllNewMessages(recipientId));
        return ResponseEntity.ok(responseBody);
    }

//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("api/messages/{senderId}/{recipientId}")
    public ResponseEntity<Object> findChatMessages (@PathVariable Integer senderId,
                                                @PathVariable Integer recipientId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }

//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("api/messages/{id}")
    public ResponseEntity<Object> findMessage(@PathVariable(name = "id") int id) throws ResourceNotFoundException {
        return ResponseEntity.ok(chatMessageService.findById(id));
    }

    @GetMapping("api/contacts/members")
    public ResponseEntity<Object> getMemberContacts(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "size", defaultValue = "5") int size) throws InvalidInputException {
        PaginationResponse<ContactResponse> responseBody = userService.findContacts(page, size, ERole.ROLE_ADMIN, false);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("api/contacts/admins")
    public ResponseEntity<Object> getAdminContacts(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "size", defaultValue = "5") int size) throws InvalidInputException {
        PaginationResponse<ContactResponse> responseBody = userService.findContacts(page, size, ERole.ROLE_USER, false);
        return ResponseEntity.ok(responseBody);
    }
}
