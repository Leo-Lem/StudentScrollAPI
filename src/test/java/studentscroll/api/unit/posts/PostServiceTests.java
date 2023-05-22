package studentscroll.api.unit.posts;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import studentscroll.api.posts.data.ContentPost;
import studentscroll.api.account.data.Student;
import studentscroll.api.account.data.StudentRepository;
import studentscroll.api.posts.data.*;
import studentscroll.api.posts.services.PostService;
import studentscroll.api.shared.StudentLocation;

public class PostServiceTests {

  @Mock
  private PostRepository repo;

  @Mock
  private StudentRepository studentRepo;

  @InjectMocks
  private PostService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenPostExists_whenDeletingPost_thenDoesNotThrow() {
    Post post = exampleContentPost();

    when(repo.findById(post.getId()))
        .thenReturn(Optional.of(post));

    assertDoesNotThrow(() -> service.delete(post.getId()));
  }

  @Test
  public void givenPostDoesNotExist_whenDeletingPost_thenThrowsEntityNotFoundException() {
    val postId = 1L;

    when(repo.findById(postId))
        .thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.delete(postId));
  }

  @Test
  public void givenPostExists_whenReadingById_thenReturnsPost() {
    Post post = exampleContentPost();

    when(repo.findById(post.getId()))
        .thenReturn(Optional.of(post));

    Post readPost = service.read(post.getId());

    assertEquals(post.getId(), readPost.getId());
    assertEquals(post.getPoster().getId(), readPost.getPoster().getId());
    assertEquals(post.getTimestamp(), readPost.getTimestamp());
    assertEquals(post.getTags(), readPost.getTags());
    assertEquals(post.getTitle(), readPost.getTitle());
    assertEquals(((ContentPost) post).getContent(), ((ContentPost) readPost).getContent());
  }

  @Test
  public void givenPostDoesNotExist_whenReadingById_thenThrowsEntityNotFoundException() {
    when(repo.findById(anyLong()))
      .thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.read(1L));
  }

  @Test
  public void whenCreatingEventPost_thenReturnsCorrectPost() {
    EventPost post = exampleEventPost();

    when(repo.save(any(EventPost.class)))
        .thenReturn(post);

    when(studentRepo.findById(anyLong()))
        .thenAnswer(i -> Optional.of(new Student().setId((Long) i.getArguments()[0])));

    EventPost createdPost = service.create(
        post.getPoster().getId(), post.getTitle(), post.getTags(), post.getDescription(), post.getDate(),
        post.getLocation());

    assertEquals(post.getPoster().getId(), createdPost.getPoster().getId());
    assertEquals(post.getTitle(), createdPost.getTitle());
    assertEquals(post.getDescription(), createdPost.getDescription());
    assertEquals(post.getDate(), createdPost.getDate());
    assertEquals(post.getLocation(), createdPost.getLocation());
    assertEquals(post.getTags(), createdPost.getTags());
  }

  @Test
  public void givenPostExists_whenUpdatingPost_thenReturnsUpdatedContentPost() {
    String newTitle = "New title", newDescription = "New content here";
    EventPost post = exampleEventPost();

    when(repo.findById(post.getId()))
        .thenReturn(Optional.of(post));

    when(repo.save(any(EventPost.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    Post updatedPost = service.update(
        post.getId(),
        Optional.of(newTitle), Optional.empty(),
        Optional.of(newDescription), Optional.empty(), Optional.empty(),
        Optional.empty());

    assertEquals(post.getPoster().getId(), updatedPost.getPoster().getId());
    assertEquals(newTitle, updatedPost.getTitle());
    assertEquals(post.getTags(), updatedPost.getTags());
  }

  @Test
  public void givenPostDoesNotExist_whenUpdatingPost_thenThrowsEntityNotFoundException() {
    when(repo.findById(anyLong()))
        .thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.update(
      1L, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
  }

  @Test
  public void whenCreatingContentPost_thenReturnsCorrectPost() {
    ContentPost post = exampleContentPost();

    when(repo.save(any(ContentPost.class)))
        .thenReturn(post);

    when(studentRepo.findById(anyLong()))
        .thenAnswer(i -> Optional.of(new Student().setId((Long) i.getArguments()[0])));

    ContentPost createdPost = service.create(
        post.getPoster().getId(), post.getTitle(), post.getTags(), post.getContent());

    assertEquals(post.getPoster().getId(), createdPost.getPoster().getId());
    assertEquals(post.getTitle(), createdPost.getTitle());
    assertEquals(post.getContent(), createdPost.getContent());
    assertEquals(post.getTags(), createdPost.getTags());
  }

  private ContentPost exampleContentPost() {
    return (ContentPost) new ContentPost(
        "Jimmy's Dog",
        Set.of("JIMMY", "DOG"),
        "Jimmy's dog is really cute. I would love to pet it again.")
        .setPoster(new Student().setId(1L))
        .setId(1L);
  }

  private EventPost exampleEventPost() {
    return (EventPost) new EventPost(
        "Petting Jimmy's Dog", Set.of("JIMMY", "DOG"),
        "Going to Jimmy's house to pet his dog.",
        LocalDateTime.now(),
        new StudentLocation("Jimmy's House", 1.0, 1.0))
        .setPoster(new Student().setId(1L))
        .setId(1L);
  }

}
