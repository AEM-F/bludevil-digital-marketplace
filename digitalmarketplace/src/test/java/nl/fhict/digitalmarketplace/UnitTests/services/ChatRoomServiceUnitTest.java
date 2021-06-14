package nl.fhict.digitalmarketplace.UnitTests.services;

import nl.fhict.digitalmarketplace.customException.ResourceNotFoundException;
import nl.fhict.digitalmarketplace.model.chat.ChatMessage;
import nl.fhict.digitalmarketplace.model.chat.ChatRoom;
import nl.fhict.digitalmarketplace.model.chat.MessageStatus;
import nl.fhict.digitalmarketplace.repository.chat.ChatMessageRepository;
import nl.fhict.digitalmarketplace.repository.chat.ChatRoomRepository;
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

import static org.junit.Assert.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ChatRoomServiceUnitTest {
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @Mock
    private ChatMessageRepository chatMessageRepository;
    @InjectMocks
    private ChatRoomService chatRoomService;

    @Before
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getChatIdWithExistingChatTest() throws Exception{
        // arrange
        String chatId = String.format("%s_%s", 1, 2);
        ChatRoom room = new ChatRoom();
        room.setChatId(chatId);
        room.setSenderId(1);
        room.setRecipientId(2);

        Mockito.when(chatRoomRepository.findBySenderIdAndRecipientId(1, 2)).thenReturn(room);
        // act
        String returnedChatId = chatRoomService.getChatId(1, 2, true);
        // assert
        assertEquals("1_2", returnedChatId);
    }

    @Test
    public void getChatIdWithCreationTest() throws Exception{
        // arrange
        Mockito.when(chatRoomRepository.findBySenderIdAndRecipientId(1, 2)).thenReturn(null);
        // act
        String returnedChatId = chatRoomService.getChatId(1, 2, true);
        // assert
        assertEquals("1_2", returnedChatId);
    }

    @Test
    public void getChatIdWithoutCreationTest() throws Exception{
        // arrange
        Mockito.when(chatRoomRepository.findBySenderIdAndRecipientId(1, 2)).thenReturn(null);
        // act
        String returnedChatId = chatRoomService.getChatId(1, 2, false);
        // assert
        assertNull(returnedChatId);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deleteChatWithInvalidChatIdTest() throws Exception{
        // arrange
        String chatId = String.format("%s_%s", 1, 2);

        ChatRoom senderRecipient = new ChatRoom();
        senderRecipient.setChatId(chatId);
        senderRecipient.setSenderId(1);
        senderRecipient.setRecipientId(2);

        ChatRoom recipientSender = new ChatRoom();
        recipientSender.setChatId(chatId);
        recipientSender.setSenderId(2);
        recipientSender.setRecipientId(1);

        List<ChatRoom> rooms = new ArrayList<>();
        rooms.add(senderRecipient);
        rooms.add(recipientSender);
        Mockito.when(chatRoomRepository.findAllByChatId(chatId)).thenReturn(null);
        // act
        chatRoomService.deleteChat(chatId);
        // assert
    }
}
