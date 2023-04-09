package studentscroll.api.unit.posts;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import lombok.val;
import studentscroll.api.posts.data.*;
import studentscroll.api.posts.services.*;
import studentscroll.api.posts.web.PostsRestController;
import studentscroll.api.posts.web.dto.*;
import studentscroll.api.shared.Location;

public class PostsRestControllerTests {

  @Mock
  private PostService postService;

  @Mock
  private EventPostService eventPostService;

  @Mock
  private ContentPostService contentPostService;

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

    when(contentPostService.create(anyLong(), any(), any(), any()))
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

  }

  @Test
  public void givenPostDoesNotExist_whenReadingById_thenReturns404() {

  }

  @Test
  public void givenPostExists_whenUpdatingById_thenReturns200AndUpdatedPost() {

  }

  @Test
  public void givenPostDoesNotExist_whenUpdatingById_thenReturns404() {

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
