package studentscroll.api.unit.posts;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.posts.data.ContentPost;
import studentscroll.api.posts.data.*;
import studentscroll.api.posts.services.PostService;
import studentscroll.api.shared.Location;

public class PostServiceTests {

  @Mock
  private PostRepository repo;

  @InjectMocks
  private PostService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenPostExists_whenDeletingPost_thenDoesNotThrow() {
    Long postID = 1L;

    when(repo.existsById(postID))
        .thenReturn(true);

    assertDoesNotThrow(() -> service.delete(postID));
  }

  @Test
  public void givenPostDoesNotExist_whenDeletingPost_thenThrowsEntityNotFoundException() {
    Long postID = 1L;

    when(repo.existsById(postID))
        .thenReturn(false);

    assertThrows(EntityNotFoundException.class, () -> service.delete(1L));
  }

  @Test
  public void givenPostExists_whenReadingById_thenReturnsPost() {
    Post post = exampleContentPost();

    when(repo.findById(post.getId()))
        .thenReturn(Optional.of(post));

    Post readPost = service.read(post.getId());

    assertEquals(post.getId(), readPost.getId());
    assertEquals(post.getPosterId(), readPost.getPosterId());
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

    EventPost createdPost = service.create(
        post.getPosterId(), post.getTitle(), post.getTags(), post.getDescription(), post.getDate(), post.getLocation());

    assertEquals(post.getPosterId(), createdPost.getPosterId());
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

    assertEquals(post.getPosterId(), updatedPost.getPosterId());
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

    ContentPost createdPost = service.create(post.getPosterId(), post.getTitle(), post.getTags(), post.getContent());

    assertEquals(post.getPosterId(), createdPost.getPosterId());
    assertEquals(post.getTitle(), createdPost.getTitle());
    assertEquals(post.getContent(), createdPost.getContent());
    assertEquals(post.getTags(), createdPost.getTags());
  }

  private ContentPost exampleContentPost() {
    return new ContentPost(
        1L, "Jimmy's Dog", Set.of("JIMMY", "DOG"), "Jimmy's dog is really cute. I would love to pet it again.");
  }

  private EventPost exampleEventPost() {
    return new EventPost(
        1L, "Petting Jimmy's Dog", Set.of("JIMMY", "DOG"),
        "Going to Jimmy's house to pet his dog.",
        LocalDate.now(),
        new Location("Jimmy's House", 1.0, 1.0));
  }

}
