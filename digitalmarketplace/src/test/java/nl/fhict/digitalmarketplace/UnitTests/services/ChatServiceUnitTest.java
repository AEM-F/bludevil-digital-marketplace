package nl.fhict.digitalmarketplace.UnitTests.services;

import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.chat.ChatMessage;
import nl.fhict.digitalmarketplace.model.chat.MessageStatus;
import nl.fhict.digitalmarketplace.model.user.User;
import nl.fhict.digitalmarketplace.repository.chat.ChatMessageRepository;
import nl.fhict.digitalmarketplace.repository.user.UserRepository;
import nl.fhict.digitalmarketplace.service.chat.ChatMessageService;
import nl.fhict.digitalmarketplace.service.chat.ChatRoomService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ChatServiceUnitTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChatMessageRepository chatMessageRepository;
    @Mock
    private ChatRoomService chatRoomService;
    @InjectMocks
    private ChatMessageService chatMessageService;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void saveMessageTest() throws Exception{
        // arrange
        String chatId = String.format("%s_%s", 1, 2);
        ChatMessage message = new ChatMessage();
        message.setSenderName("testS");
        message.setRecipientName("testR");
        message.setMessageId(1);
        message.setChatId(chatId);
        message.setSenderId(1);
        message.setRecipientId(2);
        message.setStatus(MessageStatus.DELIVERED);
        message.setContent("hello");
        message.setTimestamp(LocalDateTime.now());
        Mockito.when(chatMessageRepository.save(message)).thenReturn(message);
        // act
        ChatMessage returnedMessage = chatMessageService.save(message);
        // assert
        assertEquals(MessageStatus.RECEIVED, returnedMessage.getStatus());
    }

    @Test
    public void countNewMessagesTest() throws Exception{
        // arrange
        Mockito.when(userRepository.getById(1)).thenReturn(new User());
        Mockito.when(userRepository.getById(2)).thenReturn(new User());
        Mockito.when(chatMessageRepository.countBySenderIdAndRecipientIdAndStatus(1, 2, MessageStatus.RECEIVED)).thenReturn(1L);
        // act
        long newMessages = chatMessageService.countNewMessages(1, 2);
        // assert
        assertEquals(1L, newMessages);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void countNewMessagesWithNoFoundUsersTest() throws Exception{
        // arrange
        Mockito.when(userRepository.getById(1)).thenReturn(null);
        Mockito.when(userRepository.getById(2)).thenReturn(new User());
        // act
        chatMessageService.countNewMessages(1, 2);
        // assert
    }

    @Test
    public void countAllNewMessagesTest() throws Exception{
        // arrange
        Mockito.when(userRepository.getById(1)).thenReturn(new User());
        Mockito.when(chatMessageRepository.countByRecipientIdAndStatus(1, MessageStatus.RECEIVED)).thenReturn(1L);
        // act
        long newMessages = chatMessageService.countAllNewMessages(1);
        // assert
        assertEquals(1L, newMessages);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void countAllNewMessagesWithUserTest() throws Exception{
        // arrange
        Mockito.when(userRepository.getById(1)).thenReturn(null);
        // act
        chatMessageService.countAllNewMessages(1);
        // assert
    }

    @Test
    public void findChatMessages() throws Exception{
        // arrange
        List<ChatMessage> messageList = new ArrayList<>();
        String chatId = String.format("%s_%s", 1, 2);
        ChatMessage message = new ChatMessage();
        message.setSenderName("testS");
        message.setRecipientName("testR");
        message.setMessageId(1);
        message.setChatId(chatId);
        message.setSenderId(1);
        message.setRecipientId(2);
        message.setStatus(MessageStatus.RECEIVED);
        message.setContent("hello");
        message.setTimestamp(LocalDateTime.now());
        messageList.add(message);
        Mockito.when(chatRoomService.getChatId(1, 2, false)).thenReturn(chatId);
        Mockito.when(chatMessageRepository.findAllByChatId(chatId)).thenReturn(messageList);
        Mockito.when(chatMessageRepository.findAllBySenderIdAndRecipientId(1, 2)).thenReturn(messageList);
        // act
        List<ChatMessage> returnedMessages = chatMessageService.findChatMessages(1, 2);
        // assert
        assertEquals(1, returnedMessages.size());
    }

    @Test
    public void findByIdTest() throws Exception{
        // arrange
        String chatId = String.format("%s_%s", 1, 2);
        ChatMessage message = new ChatMessage();
        message.setSenderName("testS");
        message.setRecipientName("testR");
        message.setMessageId(1);
        message.setChatId(chatId);
        message.setSenderId(1);
        message.setRecipientId(2);
        message.setStatus(MessageStatus.RECEIVED);
        message.setContent("hello");
        message.setTimestamp(LocalDateTime.now());
        Mockito.when(chatMessageRepository.getByMessageId(1)).thenReturn(message);
        Mockito.when(chatMessageRepository.save(message)).thenReturn(message);
        // act
        ChatMessage returnedMessage = chatMessageService.findById(1);
        // assert
        assertNotNull(returnedMessage);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByIdWithNoMessageTest() throws Exception{
        // arrange
        Mockito.when(chatMessageRepository.getByMessageId(1)).thenReturn(null);
        // act
        chatMessageService.findById(1);
        // assert
    }
}
