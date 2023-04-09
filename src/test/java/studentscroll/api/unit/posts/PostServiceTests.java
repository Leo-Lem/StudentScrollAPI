package studentscroll.api.unit.posts;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.posts.data.ContentPost;
import studentscroll.api.posts.data.*;
import studentscroll.api.posts.services.PostService;

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
    Post post = examplePost();

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

  private Post examplePost() {
    return new ContentPost(
        1L, "Jimmy's Dog", Set.of("JIMMY", "DOG"), "Jimmy's dog is really cute. I would love to pet it again.");
  }

}
