package studentscroll.api.unit.chats;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import studentscroll.api.account.data.Student;
import studentscroll.api.chats.data.Chat;
import studentscroll.api.chats.data.Message;
import studentscroll.api.chats.services.MessageService;
import studentscroll.api.chats.web.MessagesRestController;

public class MessagesRestControllerTests {

  @Mock
  private MessageService service;

  @InjectMocks
  private MessagesRestController controller;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenChatAndSenderExists_whenCreatingMessage_thenReturnsMessageResponse() throws Exception {
    val chatId = 1L;
    val student = new Student();
    val content = "Hello, world!";

    when(service.create(student, content, chatId))
        .thenReturn(
            new Message(content)
                .setSender(new Student().setId(1L))
                .setChat(new Chat().setId(1L)));

    val response = controller.create(chatId, content, mock(HttpServletResponse.class));

    assertEquals(response.getContent(), content);
  }

  @Test
  public void givenMessageExists_whenReadingMessage_thenReturnsMessageResponse() {
    val messageId = 1L;

    when(service.read(messageId))
        .thenReturn(
            new Message("Hello, world!")
                .setId(messageId)
                .setSender(new Student().setId(1L))
                .setChat(new Chat().setId(1L)));

    val response = controller.read(messageId);

    assertEquals(response.getContent(), "Hello, world!");
  }

  @Test
  public void givenMessageExists_whenUpdatingMessage_thenReturnsMessageResponse() {
    val messageId = 1L;
    val newContent = "Hello, world!";

    when(service.update(messageId, newContent))
        .thenReturn(
            new Message(newContent)
                .setId(messageId)
                .setSender(new Student().setId(1L))
                .setChat(new Chat().setId(1L)));

    val response = controller.update(messageId, newContent);

    assertEquals(response.getContent(), newContent);
  }

  @Test
  public void givenChatOrStudentDoesNotExist_whenCreatingMessage_thenThrows() {

  }

  @Test
  public void givenMessageDoesNotExist_whenGettingOrUpdatingOrDeletingMessage_thenThrows() {

  }

}
