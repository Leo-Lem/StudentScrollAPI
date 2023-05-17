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
public class RUSettingsITest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void puttingAndGettingSettings() throws Exception {
    mockMVC.perform(
        post("/students")
            .contentType("application/json")
            .content(objectMapper
                .writeValueAsString(new CreateStudentRequest("John Silver", "abc@xyz.com", "1234"))))
        .andExpect(jsonPath("$.id").value(1L));

    mockMVC.perform(
        put("/students/1/settings")
            .contentType("application/json")
            .content(objectMapper
                .writeValueAsString(
                    new UpdateSettingsRequest("DARK", "DE"))))
        .andExpect(status().isOk());

    mockMVC.perform(get("/students/1/settings"))
        .andExpect(status().isOk());
  }

}
