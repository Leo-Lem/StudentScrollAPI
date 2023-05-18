package studentscroll.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.val;
import studentscroll.api.students.web.dto.CreateStudentRequest;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext
public class CRDFollowerITest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void test() throws Exception {
    Long firstStudentId = 1L, secondStudentId = 2L;

    getFollowers(firstStudentId).andExpect(status().isNotFound());
    getFollows(firstStudentId).andExpect(status().isNotFound());
    follow(firstStudentId, secondStudentId).andExpect(status().isNotFound());
    unfollow(firstStudentId, secondStudentId).andExpect(status().isNotFound());

    createStudent("abc@xyz.com").andExpect(jsonPath("$.id").value(firstStudentId));
    createStudent("xyz@abc.com").andExpect(jsonPath("$.id").value(secondStudentId));

    getFollowers(firstStudentId).andExpect(content().json("[]"));
    getFollows(firstStudentId).andExpect(content().json("[]"));

    follow(firstStudentId, secondStudentId).andExpect(status().isCreated());
    getFollowers(secondStudentId).andExpect(content().json("[1]"));

    follow(secondStudentId, firstStudentId).andExpect(status().isCreated());
    getFollows(firstStudentId).andExpect(content().json("[2]"));

    unfollow(secondStudentId, firstStudentId).andExpect(status().isNoContent());
    getFollows(secondStudentId).andExpect(content().json("[]"));

    unfollow(firstStudentId, secondStudentId).andExpect(status().isNoContent());
    getFollowers(secondStudentId).andExpect(content().json("[]"));
  }

  private ResultActions getFollowers(Long id) throws Exception {
    return mockMVC.perform(get("/students/" + id + "/followers"));
  }

  private ResultActions getFollows(Long id) throws Exception {
    return mockMVC.perform(get("/students/" + id + "/follows"));
  }

  private ResultActions follow(Long followerId, Long followsId) throws Exception {
    return mockMVC.perform(post("/students/" + followsId + "/followers/" + followerId));
  }

  private ResultActions unfollow(Long followerId, Long followsId) throws Exception {
    return mockMVC.perform(delete("/students/" + followsId + "/followers/" + followerId));
  }

  private ResultActions createStudent(String email) throws Exception {
    val request = new CreateStudentRequest("John Silver", email, "1234");

    return mockMVC.perform(
        post("/students")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)));
  }

}
