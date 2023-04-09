package studentscroll.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.val;
import studentscroll.api.students.web.dto.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class PuttingAndGettingProfileIntegrationTest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void puttingAndGettingProfile() throws Exception {
    createStudent();

    val request = new UpdateProfileRequest("Donny", "Hello, I'm Donny.", "SHARK", null, null);

    mockMVC.perform(
        put("/students/1/profile")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    mockMVC.perform(get("/students/1/profile"))
        .andExpect(status().isOk());
  }

  private void createStudent() throws Exception {
    val registerRequest = new CreateStudentRequest("John Silver", "abc@xyz.com", "1234");

    mockMVC.perform(
        post("/students")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(registerRequest)));
  }

}
