package studentscroll.api.unit.posts;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import studentscroll.api.posts.data.*;
import studentscroll.api.posts.services.*;
import studentscroll.api.posts.web.PostsRestController;
import studentscroll.api.posts.web.dto.*;
import studentscroll.api.shared.Location;

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
  public void whenCreatingPost_thenReturns201AndCorrectFields() {
    val post = exampleContentPost();
    val request = new CreatePostRequest(
        post.getPosterId(), post.getTitle(), post.getTags().toArray(new String[] {}),
        null, null, null, post.getContent());

    when(service.create(anyLong(), any(), any(), any()))
        .thenReturn(post);

    ResponseEntity<?> response = controller.create(request);

    assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());

    PostResponse body = (PostResponse) response.getBody();
    if (body != null) {
      assertEquals(post.getPosterId(), body.getPosterId());
      assertEquals(post.getTitle(), body.getTitle());
      assertEquals(post.getTags().size(), body.getTags().length);
      assertEquals(post.getContent(), body.getContent());
    }
  }

  @Test
  public void whenCreatingInvalidPost_thenReturns400() {
    val invalidRequest = new CreatePostRequest(
        1L, "something", new String[] {}, null, null, null, null);

    assertEquals(HttpStatusCode.valueOf(400), controller.create(invalidRequest).getStatusCode());
  }

  @Test
  public void givenPostExist_whenReadingById_thenReturns200() {
    Long postId = 1L;

    when(service.read(postId))
        .thenReturn(exampleEventPost());

    val response = controller.read(postId);
    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    assertTrue(response.hasBody());
  }

  @Test
  public void givenPostDoesNotExist_whenReadingById_thenReturns404() {
    Long postId = 1L;

    when(service.read(postId))
        .thenThrow(new EntityNotFoundException());

    assertEquals(HttpStatusCode.valueOf(404), controller.read(postId).getStatusCode());
  }

  @Test
  public void givenPostExists_whenUpdatingById_thenReturns200AndUpdatedPost() {
    Long postId = 1L;
    String newTitle = "Some title", newDescription = "some description";
    EventPost post = exampleEventPost();
    val request = new UpdatePostRequest(newTitle, null, newDescription, null, null, null);

    when(service.update(anyLong(), any(), any(), any(), any(), any(), any()))
        .thenReturn(post);

    ResponseEntity<?> response = controller.update(postId, request);

    assertEquals(HttpStatusCode.valueOf(200), controller.update(postId, request).getStatusCode());

    PostResponse body = (PostResponse) response.getBody();
    if (body != null) {
      assertEquals(post.getPosterId(), body.getPosterId());
      assertEquals(post.getTitle(), body.getTitle());
      assertEquals(post.getTags().size(), body.getTags().length);
      assertEquals(post.getDescription(), body.getDescription());
    }
  }

  @Test
  public void givenPostDoesNotExist_whenUpdatingById_thenReturns404() {
    Long postId = 1L;
    String newTitle = "Some title", newDescription = "some description";
    val request = new UpdatePostRequest(newTitle, null, newDescription, null, null, null);

    when(service.update(anyLong(), any(), any(), any(), any(), any(), any()))
        .thenThrow(new EntityNotFoundException());

    assertEquals(HttpStatusCode.valueOf(404), controller.update(postId, request).getStatusCode());
  }

  @Test
  public void givenPostExists_whenDeletingById_thenReturns200() {

  }

  @Test
  public void givenPostDoesNotExist_whenDeletingById_thenReturns404() {

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
