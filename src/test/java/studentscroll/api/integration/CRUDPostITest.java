package studentscroll.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

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

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext
public class CRUDPostITest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  // @Test
  // public void test() throws Exception {
  // Long studentId = createStudent("abc");

  // Long contentPostId = 1L, eventPostId = 2L;

  // getPost(contentPostId).andExpect(status().isNotFound());
  // updateContentPost(contentPostId).andExpect(status().isNotFound());
  // deletePost(contentPostId).andExpect(status().isNotFound());

  // createContentPost(studentId)
  // .andExpect(status().isCreated())
  // .andExpect(jsonPath("$.id").value(contentPostId));

  // createEventPost(studentId)
  // .andExpect(status().isCreated())
  // .andExpect(jsonPath("$.id").value(eventPostId));

  // createInvalidPost(studentId)
  // .andExpect(status().isBadRequest());

  // getPost(contentPostId).andExpect(status().isOk());
  // updateContentPost(contentPostId).andExpect(status().isOk());

  // getPost(eventPostId).andExpect(status().isOk());
  // updateEventPost(eventPostId).andExpect(status().isOk());

  // deletePost(contentPostId).andExpect(status().isNoContent());
  // deletePost(contentPostId).andExpect(status().isNotFound());

  // deletePost(eventPostId).andExpect(status().isNoContent());
  // deletePost(eventPostId).andExpect(status().isNotFound());
  // }

  private ResultActions createContentPost(Long posterId) throws Exception {
    val request = new CreatePostRequest(
        posterId, "Title", new String[] { "TAG", "ANOTHER_TAG" }, null, null, null,
        "Some content here");

    return createPost(request);
  }

  private ResultActions createEventPost(Long posterId) throws Exception {
    val request = new CreatePostRequest(
        posterId, "Title", new String[] { "TAG", "ANOTHER_TAG" },
        "Some description here", LocalDateTime.now(), new StudentLocation(1.0, 1.0), null);

    return createPost(request);
  }

  private ResultActions createInvalidPost(Long posterId) throws Exception {
    val request = new CreatePostRequest(posterId, "Some title", new String[] {}, null, null,
        null, null);

    return createPost(request);
  }

  private ResultActions getPost(Long id) throws Exception {
    return mockMVC.perform(
        get("/posts/" + id));
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
