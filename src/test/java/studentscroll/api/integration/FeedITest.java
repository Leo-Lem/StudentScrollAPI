package studentscroll.api.integration;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.val;
import studentscroll.api.posts.web.dto.CreatePostRequest;
import studentscroll.api.students.data.Profile;
import studentscroll.api.students.data.Student;
import studentscroll.api.students.web.dto.CreateStudentRequest;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext
public class FeedITest {

  @Mock
  private Authentication authentication;

  @Mock
  private SecurityContext securityContext;

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void givenMultiplePostsAndStudentIsFollowing1_whenReadingAllPosts_thenOlderPostsByFollowerAreReturnFirst()
      throws Exception {

    Long studentId = createStudent("abc");
    Long followId = createStudent("bca");
    Long nonfollowId = createStudent("cba");

    createContentPost(nonfollowId);
    createContentPost(followId);
    createContentPost(nonfollowId);
    createContentPost(followId);
    createContentPost(nonfollowId);
    createContentPost(followId);
    createContentPost(nonfollowId);
    createContentPost(followId);
    createContentPost(nonfollowId);
    createContentPost(followId);

    follow(studentId, followId);

    getAllPosts().andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content[0].posterId").value(followId))
        .andExpect(jsonPath("$.content[2].posterId").value(followId))
        .andExpect(jsonPath("$.content[4].posterId").value(followId))
        .andExpect(jsonPath("$.content[6].posterId").value(followId))
        .andExpect(jsonPath("$.content[8].posterId").value(followId))
        .andExpect(jsonPath("$.content[1].posterId").value(nonfollowId))
        .andExpect(jsonPath("$.content[3].posterId").value(nonfollowId))
        .andExpect(jsonPath("$.content[5].posterId").value(nonfollowId))
        .andExpect(jsonPath("$.content[7].posterId").value(nonfollowId))
        .andExpect(jsonPath("$.content[9].posterId").value(nonfollowId));
  }

  private void follow(Long studentId, Long followId) {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(
      new Student().setId(studentId)
      .setProfile(new Profile().setFollows(List.of(new Student().setId(followId)))));
    SecurityContextHolder.setContext(securityContext);
  }

  private ResultActions getAllPosts() throws Exception {
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    return mockMVC.perform(
        get("/posts"));
  }

  private ResultActions createContentPost(Long posterId) throws Exception {
    val request = new CreatePostRequest(
        posterId, "Title", new String[] { "TAG", "ANOTHER_TAG" }, null, null, null,
        "Some content here");

    return createPost(request);
  }

  private ResultActions createPost(CreatePostRequest request) throws Exception {
    return mockMVC.perform(
        post("/posts")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)));
  }

  private Long createStudent(String email) throws Exception {
    val request = new CreateStudentRequest("John Silver", email, "1234");

    return objectMapper.readTree(
        mockMVC.perform(
            post("/students")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(jsonPath("$.id").isNumber()).andReturn().getResponse()
            .getContentAsString())
        .get("id").asLong();
  }

}
