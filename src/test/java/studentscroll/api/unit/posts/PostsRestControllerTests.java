package studentscroll.api.unit.posts;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import studentscroll.api.account.data.Student;
import studentscroll.api.posts.data.*;
import studentscroll.api.posts.services.*;
import studentscroll.api.posts.web.PostsRestController;
import studentscroll.api.posts.web.dto.*;
import studentscroll.api.shared.StudentLocation;

public class PostsRestControllerTests {

  @Mock
  private PostService service;

  @InjectMocks
  private PostsRestController controller;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void whenCreatingPost_thenReturnsCorrectFields() {
    val post = exampleContentPost();
    val request = new CreatePostRequest(
        post.getPoster().getId(), post.getTitle(), post.getTags().toArray(new String[] {}),
        null, null, null, post.getContent());

    when(service.create(anyLong(), any(), any(), any()))
        .thenReturn(post);

    PostResponse response = controller.create(request, mock(HttpServletResponse.class));

    assertEquals(post.getPoster().getId(), response.getPosterId());
    assertEquals(post.getTitle(), response.getTitle());
    assertEquals(post.getTags().size(), response.getTags().length);
    assertEquals(post.getContent(), response.getContent());
  }

  @Test
  public void whenCreatingInvalidPost_thenThrowsResponseStatusException() {
    val invalidRequest = new CreatePostRequest(1L, "something", new String[] {}, null, null, null, null);

    assertThrows(ResponseStatusException.class,
        () -> controller.create(invalidRequest, mock(HttpServletResponse.class)));
  }

  @Test
  public void givenPostExist_whenReadingById_thenDoesNotThrow() {
    Long postId = 1L;

    when(service.read(postId))
        .thenReturn(exampleEventPost());

    assertDoesNotThrow(() -> controller.read(postId));
  }

  @Test
  public void givenPostExists_whenUpdatingById_thenReturns200AndUpdatedPost() {
    Long postId = 1L;
    String newTitle = "Some title", newDescription = "some description";
    EventPost post = exampleEventPost();
    val request = new UpdatePostRequest(newTitle, null, newDescription, null, null, null);

    when(service.update(anyLong(), any(), any(), any(), any(), any(), any()))
        .thenReturn(post);

    PostResponse response = controller.update(postId, request);
    assertEquals(post.getPoster().getId(), response.getPosterId());
    assertEquals(post.getTitle(), response.getTitle());
    assertEquals(post.getTags().size(), response.getTags().length);
    assertEquals(post.getDescription(), response.getDescription());
  }

  @Test
  public void givenPostExists_whenDeletingById_thenDoesntThrow() {
    assertDoesNotThrow(() -> controller.delete(1L));
  }

  @Test
  public void givenPostDoesNotExist_whenReadingUpdatingDeletingById_thenThrowsEntityNotFoundException() {
    Long postId = 1L;

    val request = new UpdatePostRequest("", null, "", null, null, null);

    when(service.read(postId))
        .thenThrow(new EntityNotFoundException());

    when(service.update(anyLong(), any(), any(), any(), any(), any(), any()))
        .thenThrow(new EntityNotFoundException());

    doThrow(new EntityNotFoundException())
        .when(service).delete(postId);

    assertThrows(EntityNotFoundException.class, () -> controller.read(postId));
    assertThrows(EntityNotFoundException.class, () -> controller.update(postId, request));
    assertThrows(EntityNotFoundException.class, () -> controller.delete(postId));
  }

  private ContentPost exampleContentPost() {
    return (ContentPost) new ContentPost(
        "Jimmy's Dog",
        Set.of("JIMMY", "DOG"),
        "Jimmy's dog is really cute. I would love to pet it again.")
        .setPoster(new Student().setId(1L));
  }

  private EventPost exampleEventPost() {
    return (EventPost) new EventPost(
        "Petting Jimmy's Dog", Set.of("JIMMY", "DOG"),
        "Going to Jimmy's house to pet his dog.",
        LocalDateTime.now(),
        new StudentLocation("Jimmy's House", 1.0, 1.0))
        .setPoster(new Student().setId(1L));
  }

}
