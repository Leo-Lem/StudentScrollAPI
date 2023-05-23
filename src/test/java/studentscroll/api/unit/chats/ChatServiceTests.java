package studentscroll.api.unit.chats;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import studentscroll.api.account.data.Account;
import studentscroll.api.account.data.AccountRepository;
import studentscroll.api.chats.data.Chat;
import studentscroll.api.chats.data.ChatRepository;
import studentscroll.api.chats.services.ChatService;

public class ChatServiceTests {

  @Mock
  private ChatRepository repo;

  @Mock
  private AccountRepository studentRepo;

  @InjectMocks
  private ChatService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenParticipantsExist_whenCreatingChat_thenReturnsChat() {
    val participants = Set.of(2L, 3L);

    when(studentRepo.findById(anyLong()))
        .thenReturn(Optional.of(new Account()));

    when(repo.save(any(Chat.class)))
        .thenAnswer((i) -> i.getArgument(0));

    Chat chat = service.create(1L, participants);

    assertNotNull(chat);
  }

  @Test
  public void givenParticipantsDontExist_whenCreatingChat_thenThrowEntityNotFoundException() {
    val participants = Set.of(2L, 3L);

    when(studentRepo.findById(anyLong()))
        .thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class,
        () -> service.create(1L, participants));
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
  public void givenChatExists_whenDeletingChat_thenChatIsDeleted() {
    when(repo.findById(anyLong()))
        .thenReturn(Optional.of(new Chat()));

    assertDoesNotThrow(() -> service.delete(1L));
  }

  @Test
  public void givenChatDoesNotExist_whenReadingOrDeletingChat_thenThrowEntityNotFoundException() {
    when(repo.findById(anyLong()))
        .thenReturn(Optional.empty());

        assertThrows(
        EntityNotFoundException.class,
        () -> service.read(1L));

    assertThrows(
        EntityNotFoundException.class,
        () -> service.delete(1L));
  }

}
