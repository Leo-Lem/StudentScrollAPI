package studentscroll.api.unit.chats;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.account.data.Student;
import studentscroll.api.account.data.StudentRepository;
import studentscroll.api.chats.data.Chat;
import studentscroll.api.chats.data.ChatRepository;
import studentscroll.api.chats.data.Message;
import studentscroll.api.chats.data.MessageRepository;
import studentscroll.api.chats.services.MessageService;

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

    Message message = service.create(new Student(), "content",  1L);

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
        () -> service.create(new Student(), "content", 1L));
  }

  @Test
  public void givenSenderDoesntExist_whenCreatingMessage_thenThrowEntityNotFoundException() {
    when(repo.save(any(Message.class)))
        .thenAnswer((i) -> i.getArgument(0));

    assertThrows(
        EntityNotFoundException.class,
        () -> service.create(new Student(), "content", 1L));
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
    when(repo.findById(anyLong()))
        .thenReturn(Optional.of(new Message().setChat(new Chat())));

    assertDoesNotThrow(() -> service.delete(1L));
  }

  @Test
  public void givenMessageDoesntExist_whenDeletingMessage_thenThrowEntityNotFoundException() {
    when(repo.findById(anyLong()))
        .thenReturn(Optional.empty());

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
