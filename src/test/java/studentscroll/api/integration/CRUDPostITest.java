package studentscroll.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.val;
import studentscroll.api.posts.web.dto.*;
import studentscroll.api.shared.Location;
import studentscroll.api.students.web.dto.CreateStudentRequest;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext
public class CRUDPostITest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void crudPostIntegrationTest() throws Exception {
    createStudent();

    Long contentPostId = 1L, eventPostId = 2L;

    getPost(contentPostId).andExpect(status().isNotFound());
    updateContentPost(contentPostId).andExpect(status().isNotFound());
    deletePost(contentPostId).andExpect(status().isNotFound());

    createContentPost()
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(contentPostId));

    createEventPost()
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(eventPostId));

    createInvalidPost()
        .andExpect(status().isBadRequest());

    getPost(contentPostId).andExpect(status().isOk());
    updateContentPost(contentPostId).andExpect(status().isOk());

    getPost(eventPostId).andExpect(status().isOk());
    updateEventPost(eventPostId).andExpect(status().isOk());

    deletePost(contentPostId).andExpect(status().isNoContent());
    deletePost(contentPostId).andExpect(status().isNotFound());

    deletePost(eventPostId).andExpect(status().isNoContent());
    deletePost(eventPostId).andExpect(status().isNotFound());
  }

  private ResultActions createContentPost() throws Exception {
    val request = new CreatePostRequest(
        1L, "Title", new String[] { "TAG", "ANOTHER_TAG" }, null, null, null,
        "Some content here");

    return createPost(request);
  }

  private ResultActions createEventPost() throws Exception {
    val request = new CreatePostRequest(
        1L, "Title", new String[] { "TAG", "ANOTHER_TAG" },
        "Some description here", LocalDateTime.now(), new Location(1.0, 1.0), null);

    return createPost(request);
  }

  private ResultActions createInvalidPost() throws Exception {
    val request = new CreatePostRequest(1L, "Some title", new String[] {}, null, null,
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

  private ResultActions createStudent() throws Exception {
    val request = new CreateStudentRequest("John Silver", "abc@xyz.com", "1234");

    return mockMVC.perform(
        post("/students")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)));
  }

}
