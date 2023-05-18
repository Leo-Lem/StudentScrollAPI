package studentscroll.api.unit.chats;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.*;
import org.mockito.*;

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

}
