package studentscroll.api.unit.chats;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.*;
import org.mockito.*;

import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.chats.data.*;
import studentscroll.api.chats.services.ChatService;
import studentscroll.api.students.data.*;

public class ChatServiceTests {

  @Mock
  private ChatRepository repo;

  @Mock
  private StudentRepository studentRepo;

  @InjectMocks
  private ChatService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenParticipantsExist_whenCreatingChat_thenReturnsChat() {
    Set<Long> participants = Set.of(1L, 2L, 3L);

    when(studentRepo.findById(anyLong()))
        .thenReturn(Optional.of(new Student()));

    when(repo.save(any(Chat.class)))
        .thenAnswer((i) -> i.getArgument(0));

    Chat chat = service.create(participants);

    assertNotNull(chat);
  }

  @Test
  public void givenParticipantsDontExist_whenCreatingChat_thenThrowEntityNotFoundException() {
    Set<Long> participants = Set.of(1L, 2L, 3L);

    when(studentRepo.findById(anyLong()))
        .thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class,
        () -> service.create(participants));
  }

  @Test
  public void givenChatExists_whenReadingChat_thenReturnsChat() {
    Chat chat = new Chat();

    when(repo.findById(anyLong()))
        .thenReturn(Optional.of(chat));

    Chat result = service.read(1L);

    assertEquals(chat, result);
  }

  @Test
  public void givenChatDoesntExist_whenReadingChat_thenThrowEntityNotFoundException() {
    when(repo.findById(anyLong()))
        .thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class,
        () -> service.read(1L));
  }

  @Test
  public void givenChatExists_whenDeletingChat_thenChatIsDeleted() {

    when(repo.existsById(anyLong()))
        .thenReturn(true);

    doNothing()
        .when(repo)
        .deleteById(anyLong());

    assertDoesNotThrow(() -> service.delete(1L));
  }

  @Test
  public void givenChatDoesntExist_whenDeletingChat_thenThrowEntityNotFoundException() {
    when(repo.existsById(anyLong()))
    .thenReturn(false);

    assertThrows(
        EntityNotFoundException.class,
        () -> service.delete(1L));
  }

  @Test
  public void givenChatAndParticipantExist_whenReadingByParticipantId_thenReturnsChat() {
    Chat chat = new Chat();

    when(studentRepo.existsById(anyLong()))
        .thenReturn(true);

    when(repo.findByParticipantsId(anyLong()))
        .thenReturn(List.of(chat));

    List<Chat> chats = service.readByParticipantId(1L);

    assertEquals(chat, chats.get(0));
  }

  @Test
  public void givenStudentDoesntExist_whenReadingByParticipantId_thenThrowEntityNotFoundException() {
    when(studentRepo.existsById(anyLong()))
        .thenReturn(false);

    assertThrows(
        EntityNotFoundException.class,
        () -> service.readByParticipantId(1L));
  }

}
