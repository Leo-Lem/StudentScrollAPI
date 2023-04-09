package studentscroll.api.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.*;
import org.mockito.*;

import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.posts.data.PostRepository;
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

}
