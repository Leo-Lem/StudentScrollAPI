package studentscroll.api.unit.posts;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
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
  public void given_when_then() {

  }

}
