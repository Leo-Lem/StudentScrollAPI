package studentscroll.api.unit.posts;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.posts.data.*;
import studentscroll.api.posts.services.ContentPostService;

public class ContentPostServiceTests {

  @Mock
  private PostRepository repo;

  @InjectMocks
  private ContentPostService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void whenCreatingContentPost_thenReturnsCorrectPost() {
    ContentPost post = examplePost();

    when(repo.save(any(ContentPost.class)))
        .thenReturn(post);

    ContentPost createdPost = service.create(post.getPosterId(), post.getTitle(), post.getContent(), post.getTags());

    assertEquals(post.getPosterId(), createdPost.getPosterId());
    assertEquals(post.getTitle(), createdPost.getTitle());
    assertEquals(post.getContent(), createdPost.getContent());
    assertEquals(post.getTags(), createdPost.getTags());
  }

  @Test
  public void givenContentPostExists_whenUpdatingContentPost_thenReturnsUpdatedContentPost() {
    String newTitle = "New title", newContent = "New content here";
    ContentPost post = examplePost();

    when(repo.findById(post.getId()))
        .thenReturn(Optional.of(post));

    when(repo.save(any(ContentPost.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    ContentPost updatedPost = service.update(
        post.getId(), Optional.of(newTitle), Optional.of(newContent), Optional.empty());

    assertEquals(post.getPosterId(), updatedPost.getPosterId());
    assertEquals(newTitle, updatedPost.getTitle());
    assertEquals(newContent, updatedPost.getContent());
    assertEquals(post.getTags(), updatedPost.getTags());
  }

  @Test
  public void givenContentPostDoesNotExist_whenUpdatingContentPost_thenThrowsEntityNotFoundException() {
    when(repo.findById(anyLong()))
        .thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.update(1L, Optional.empty(), Optional.empty(), Optional.empty()));
  }

  private ContentPost examplePost() {
    return new ContentPost(
        1L, "Jimmy's Dog", Set.of("JIMMY", "DOG"), "Jimmy's dog is really cute. I would love to pet it again.");
  }

}
