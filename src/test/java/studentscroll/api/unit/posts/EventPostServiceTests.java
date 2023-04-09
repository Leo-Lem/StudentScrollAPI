package studentscroll.api.unit.posts;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.posts.data.*;
import studentscroll.api.posts.services.EventPostService;
import studentscroll.api.shared.Location;

public class EventPostServiceTests {

  @Mock
  private PostRepository repo;

  @InjectMocks
  private EventPostService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void whenCreatingContentPost_thenReturnsCorrectPost() {
    EventPost post = examplePost();

    when(repo.save(any(EventPost.class)))
        .thenReturn(post);

    EventPost createdPost = service.create(
        post.getPosterId(), post.getTitle(), post.getDescription(), post.getDate(), post.getLocation(), post.getTags());

    assertEquals(post.getPosterId(), createdPost.getPosterId());
    assertEquals(post.getTitle(), createdPost.getTitle());
    assertEquals(post.getDescription(), createdPost.getDescription());
    assertEquals(post.getDate(), createdPost.getDate());
    assertEquals(post.getLocation(), createdPost.getLocation());
    assertEquals(post.getTags(), createdPost.getTags());
  }

  @Test
  public void givenContentPostExists_whenUpdatingContentPost_thenReturnsUpdatedContentPost() {
    String newTitle = "New title", newDescription = "New content here";
    EventPost post = examplePost();

    when(repo.findById(post.getId()))
        .thenReturn(Optional.of(post));

    when(repo.save(any(EventPost.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    EventPost updatedPost = service.update(
        post.getId(),
        Optional.of(newTitle), Optional.of(newDescription), Optional.empty(), Optional.empty(), Optional.empty());

    assertEquals(post.getPosterId(), updatedPost.getPosterId());
    assertEquals(newTitle, updatedPost.getTitle());
    assertEquals(newDescription, updatedPost.getDescription());
    assertEquals(post.getDate(), updatedPost.getDate());
    assertEquals(post.getLocation(), updatedPost.getLocation());
    assertEquals(post.getTags(), updatedPost.getTags());
  }

  @Test
  public void givenContentPostDoesNotExist_whenUpdatingContentPost_thenThrowsEntityNotFoundException() {
    when(repo.findById(anyLong()))
        .thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.update(
      1L, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));
  }

  private EventPost examplePost() {
    return new EventPost(
        1L, "Petting Jimmy's Dog", Set.of("JIMMY", "DOG"),
        "Going to Jimmy's house to pet his dog.",
        LocalDate.now(),
        new Location("Jimmy's House", 1.0, 1.0));
  }

}
