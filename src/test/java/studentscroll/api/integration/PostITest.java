package studentscroll.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.val;
import studentscroll.api.posts.web.dto.CreatePostRequest;
import studentscroll.api.posts.web.dto.UpdatePostRequest;
import studentscroll.api.shared.StudentLocation;
import studentscroll.api.utils.ITestUtils;
import studentscroll.api.utils.TestUtils;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext
public class PostITest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ITestUtils utils;

  @Test
  public void test() throws Exception {
    Long studentId = utils.createStudent();
    TestUtils.authenticate(TestUtils.getStudent(studentId));

    Long contentPostId = 1L, eventPostId = 2L;
    String contentPostTitle = "Content post title";
    Set<String> tags = Set.of("tag1", "tag2", "tag3");

    getPost(contentPostId).andExpect(status().isNotFound());
    updateContentPost(contentPostId).andExpect(status().isNotFound());
    deletePost(contentPostId).andExpect(status().isNotFound());

    createContentPost(studentId)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(contentPostId));

    createEventPost(studentId)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(eventPostId));

    createInvalidPost(studentId)
        .andExpect(status().isBadRequest());

    getPost(contentPostId).andExpect(status().isOk());
    updateContentPost(contentPostId).andExpect(status().isOk());

    getPostByTitle(contentPostTitle).andExpect(status().isOk());
    getPostsByTags(tags).andExpect(status().isOk());

    getPost(eventPostId).andExpect(status().isOk());
    updateEventPost(eventPostId).andExpect(status().isOk());

    deletePost(contentPostId).andExpect(status().isNoContent());
    // deletePost(contentPostId).andExpect(status().isNotFound());

    deletePost(eventPostId).andExpect(status().isNoContent());
    // deletePost(eventPostId).andExpect(status().isNotFound());
  }

  private ResultActions createContentPost(Long posterId) throws Exception {
    val request = new CreatePostRequest(
        "Title", new String[] { "TAG", "ANOTHER_TAG" }, null, null, null, "Some content here");

    return createPost(request);
  }

  private ResultActions createEventPost(Long posterId) throws Exception {
    val request = new CreatePostRequest(
        "Title",
        new String[] { "TAG", "ANOTHER_TAG" },
        "Some description here", LocalDateTime.now(),
        new StudentLocation(1.0, 1.0),
        null);

    return createPost(request);
  }

  private ResultActions createInvalidPost(Long posterId) throws Exception {
    val request = new CreatePostRequest("Some title", new String[] {}, null, null, null, null);

    return createPost(request);
  }

  private ResultActions getPost(Long id) throws Exception {
    return mockMVC.perform(
        get("/posts/" + id));
  }

  private ResultActions getPostByTitle(String title) throws Exception {
    return mockMVC.perform(
        get("/posts?title=" + title));
  }

  private ResultActions getPostsByTags(Set<String> tags) throws Exception {
    return mockMVC.perform(
        get("/posts?tags=" + tags));
  }

  private ResultActions updateContentPost(Long id) throws Exception {
    val request = new UpdatePostRequest(
        "My new title", null, null, null, null, "some new content");

    return updatePost(id, request);
  }

  private ResultActions updateEventPost(Long id) throws Exception {
    val request = new UpdatePostRequest(
        "My new title", null, "some new content", null, null, null);

    return updatePost(id, request);
  }

  private ResultActions deletePost(Long id) throws Exception {
    return mockMVC.perform(
        delete("/posts/" + id));
  }

  private ResultActions updatePost(Long id, UpdatePostRequest request) throws Exception {
    return mockMVC.perform(
        put("/posts/" + id)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)));
  }

  private ResultActions createPost(CreatePostRequest request) throws Exception {
    return mockMVC.perform(
        post("/posts")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)));
  }

}
