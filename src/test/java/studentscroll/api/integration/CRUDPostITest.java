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

    val createContentPostRequest = new CreatePostRequest(
        1L, "Title", new String[] { "TAG", "ANOTHER_TAG" }, null, null, null,
        "Some content here");
    val updateContentPostRequest = new UpdatePostRequest(
        "My new title", null, null, null, null, "some new content");

    val createEventPostRequest = new CreatePostRequest(
        1L, "Title", new String[] { "TAG", "ANOTHER_TAG" },
        "Some description here", LocalDateTime.now(), new Location("", 1.0, 1.0), null);
    val updateEventPostRequest = new UpdatePostRequest(
        "My new title", null, "some new content", null, null, null);

    val invalidCreatePostRequest = new CreatePostRequest(1L, "Some title", new String[] {}, null, null,
        null, null);

    mockMVC.perform(
        get("/posts/1"))
        .andExpect(status().isNotFound());

    mockMVC.perform(
        put("/posts/1")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(updateContentPostRequest)))
        .andExpect(status().isNotFound());

    mockMVC.perform(
        delete("/posts/1"))
        .andExpect(status().isNotFound());

    mockMVC.perform(
        post("/posts")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(createContentPostRequest)))
        .andExpect(status().isCreated());

    mockMVC.perform(
        post("/posts")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(createEventPostRequest)))
        .andExpect(status().isCreated());

    mockMVC.perform(
        post("/posts")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(invalidCreatePostRequest)))
        .andExpect(status().isBadRequest());

    mockMVC.perform(
        get("/posts/1"))
        .andExpect(status().isOk());

    mockMVC.perform(
        put("/posts/1")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(updateContentPostRequest)))
        .andExpect(status().isOk());

    mockMVC.perform(
        put("/posts/2")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(updateEventPostRequest)))
        .andExpect(status().isOk());

    mockMVC.perform(
        delete("/posts/1"))
        .andExpect(status().isNoContent());

    mockMVC.perform(
        delete("/posts/1"))
        .andExpect(status().isNotFound());
  }

  private void createStudent() throws Exception {
    val registerRequest = new CreateStudentRequest("John Silver", "abc@xyz.com", "1234");

    mockMVC.perform(
        post("/students")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(registerRequest)));
  }

}
