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
import studentscroll.api.students.web.dto.UpdateStudentRequest;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext
public class RUDStudentIntegrationTest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void rudStudentIntegrationTest() throws Exception {
    mockMVC.perform(get("/students/1"))
        .andExpect(status().isNotFound());

    createStudent();

    val request = new UpdateStudentRequest("1234", "xyz@abc.com", null);

    mockMVC.perform(
        get("/students/1"))
        .andExpect(status().isOk());

    mockMVC.perform(
        put("/students/1")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    mockMVC.perform(
        delete("/students/1"))
        .andExpect(status().isNoContent());

    mockMVC.perform(get("/students/1"))
        .andExpect(status().isNotFound());

    mockMVC.perform(
        put("/students/1")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)))
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
