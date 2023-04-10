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

import studentscroll.api.students.web.dto.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext
public class PuttingAndGettingProfileIntegrationTest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void puttingAndGettingProfile() throws Exception {
    mockMVC.perform(
        post("/students")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(new CreateStudentRequest("John Silver", "abc@xyz.com", "1234"))))
        .andExpect(jsonPath("$.id").value(1L));

    mockMVC.perform(
        put("/students/1/profile")
            .contentType("application/json")
            .content(objectMapper
                .writeValueAsString(new UpdateProfileRequest("Donny", "Hello, I'm Donny.", "SHARK", null, null))))
        .andExpect(status().isOk());

    mockMVC.perform(get("/students/1/profile"))
        .andExpect(status().isOk());
  }

}
