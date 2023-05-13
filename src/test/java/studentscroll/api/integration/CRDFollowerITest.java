package studentscroll.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

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
  public void crdFollowerIntegrationTest() throws Exception {
    mockMVC.perform(
        get("/students/1/followers"))
        .andExpect(status().isNotFound());

    mockMVC.perform(
        get("/students/1/follows"))
        .andExpect(status().isNotFound());

    mockMVC.perform(
        post("/students/1/followers/2"))
        .andExpect(status().isNotFound());

    mockMVC.perform(
        delete("/students/1/followers/2"))
        .andExpect(status().isNotFound());

    create2Students();

    mockMVC.perform(
        get("/students/1/follows"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    mockMVC.perform(
        get("/students/2/followers"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    mockMVC.perform(
        post("/students/1/followers/2"))
        .andExpect(status().isCreated());

    mockMVC.perform(
        get("/students/1/followers"))
        .andExpect(status().isOk())
        .andExpect(content().json("[2]"));

    mockMVC.perform(
        post("/students/2/followers/1"))
        .andExpect(status().isCreated());

    mockMVC.perform(
        get("/students/1/follows"))
        .andExpect(status().isOk())
        .andExpect(content().json("[2]"));

    mockMVC.perform(
        delete("/students/2/followers/1"))
        .andExpect(status().isNoContent());

    mockMVC.perform(
        get("/students/2/followers"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));

    mockMVC.perform(
        get("/students/1/follows"))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
  }

  private void create2Students() throws Exception {
    val registerRequest = new CreateStudentRequest("John Silver", "abc@xyz.com", "1234");

    mockMVC.perform(
        post("/students")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(registerRequest)));

    val registerRequest2 = new CreateStudentRequest("James Flint", "xyz@abc.com", "1234");

    mockMVC.perform(
        post("/students")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(registerRequest2)));
  }

}
