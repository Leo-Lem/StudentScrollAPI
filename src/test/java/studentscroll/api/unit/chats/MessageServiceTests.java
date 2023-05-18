package studentscroll.api.unit.chats;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;
import org.mockito.*;

import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.chats.data.*;
import studentscroll.api.chats.services.MessageService;
import studentscroll.api.students.data.*;

public class MessageServiceTests {

  @Mock
  private MessageRepository repo;

  @Mock
  private StudentRepository studentRepo;

  @Mock
  private ChatRepository chatRepo;

  @InjectMocks
  private MessageService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenChatAndSenderExist_whenCreatingMessage_thenReturnsMessage() {
    when(studentRepo.findById(anyLong()))
        .thenReturn(Optional.of(new Student()));

    when(chatRepo.findById(anyLong()))
        .thenReturn(Optional.of(new Chat()));

    when(repo.save(any(Message.class)))
        .thenAnswer((i) -> i.getArgument(0));

    Message message = service.create("content", 1L, 1L);

    assertNotNull(message);
  }

  @Test
  public void givenChatDoesntExist_whenCreatingMessage_thenThrowEntityNotFoundException() {
    when(studentRepo.findById(anyLong()))
        .thenReturn(Optional.of(new Student()));

    when(chatRepo.findById(anyLong()))
        .thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class,
        () -> service.create("content", 1L, 1L));
  }

  @Test
  public void givenSenderDoesntExist_whenCreatingMessage_thenThrowEntityNotFoundException() {
    when(repo.save(any(Message.class)))
        .thenAnswer((i) -> i.getArgument(0));

    assertThrows(
        EntityNotFoundException.class,
        () -> service.create("content", 1L, 1L));
  }

  @Test
  public void givenMessageExists_whenReadingMessage_thenReturnsMessage() {
    when(repo.findById(anyLong()))
        .thenReturn(Optional.of(new Message()));

    Message message = service.read(1L);

    assertNotNull(message);
  }

  @Test
  public void givenMessageExists_whenDeletingMessage_thenDoesNotThrow() {
    when(repo.existsById(anyLong()))
        .thenReturn(true);

    assertDoesNotThrow(() -> service.delete(1L));
  }

  @Test
  public void givenMessageDoesntExist_whenDeletingMessage_thenThrowEntityNotFoundException() {
    when(repo.existsById(anyLong()))
        .thenReturn(false);

    assertThrows(
        EntityNotFoundException.class,
        () -> service.delete(1L));
  }

  @Test
    public void givenMessageExists_whenUpdatingMessage_thenReturnsMessage() {
      when(repo.findById(anyLong()))
          .thenReturn(Optional.of(new Message()));

      when(repo.save(any(Message.class)))
          .thenAnswer((i) -> i.getArgument(0));

      Message message = service.update(1L, "content");

      assertNotNull(message);
    }

  @Test
  public void givenMessageDoesntExist_whenUpdatingMessage_thenThrowEntityNotFoundException() {
    when(repo.findById(anyLong()))
        .thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class,
        () -> service.update(1L, "content"));
  }

}
