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
import studentscroll.api.students.web.dto.UpdateStudentRequest;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext
public class RUDStudentITest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void rudStudentIntegrationTest() throws Exception {
    val id = 1L;

    getStudent(id).andExpect(status().isNotFound());

    updateStudent(id).andExpect(status().isNotFound());

    deleteStudent(id).andExpect(status().isNotFound());

    createStudent().andExpect(jsonPath("$.id").value(id));

    getStudent(id).andExpect(status().isOk());

    updateStudent(id).andExpect(status().isOk());

    deleteStudent(id).andExpect(status().isNoContent());

    getStudent(1L).andExpect(status().isNotFound());

    updateStudent(id).andExpect(status().isNotFound());

    deleteStudent(id).andExpect(status().isNotFound());
  }

  private ResultActions createStudent() throws Exception {
    val request = new CreateStudentRequest("John Silver", "abc@xyz.com", "1234");

    return mockMVC.perform(
        post("/students")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)));
  }

  private ResultActions getStudent(Long id) throws Exception {
    return mockMVC.perform(
        get("/students/" + id));
  }

  private ResultActions updateStudent(Long id) throws Exception {
    val request = new UpdateStudentRequest("1234", "xyz@abc.com", null);
    return mockMVC.perform(
        put("/students/" + id)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)));
  }

  private ResultActions deleteStudent(Long id) throws Exception {
    return mockMVC.perform(
        delete("/students/" + id));
  }

}
