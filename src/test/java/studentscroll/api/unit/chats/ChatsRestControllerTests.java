package studentscroll.api.unit.chats;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import studentscroll.api.chats.data.Chat;
import studentscroll.api.chats.services.ChatService;
import studentscroll.api.chats.web.ChatsRestController;
import studentscroll.api.shared.NotAuthenticatedException;

public class ChatsRestControllerTests {

  @Mock
  private ChatService service;

  @InjectMocks
  private ChatsRestController controller;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenParticipantsExist_whenCreatingChat_thenReturnsChatResponse() throws NotAuthenticatedException {
    val participantIds = Set.of(2L, 3L);

    when(service.create(1L, participantIds)).thenReturn(new Chat());

    val response = controller.create(participantIds, mock(HttpServletResponse.class));

    assertNotNull(response);
  }

  @Test
  public void givenChatExists_whenReadingChat_thenReturnsChatResponse() {
    val chatId = 1L;

    when(service.read(chatId)).thenReturn(new Chat());

    val response = controller.read(chatId);

    assertNotNull(response);
  }

  @Test
  public void givenParticipantExists_whenReadingByParticipantId_thenReturnsChatResponses()
      throws NotAuthenticatedException {
    val participantId = 1L;
    val chats = List.of(new Chat(), new Chat());

    when(service.readByStudentId(participantId)).thenReturn(chats);

    val response = controller.readAll();

    assertEquals(2, response.size());
  }
}
