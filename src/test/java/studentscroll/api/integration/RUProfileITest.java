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
import studentscroll.api.shared.StudentLocation;
import studentscroll.api.students.web.dto.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext
public class RUProfileITest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  private StudentLocation location = new StudentLocation(0.0, 0.0);

  @Test
  public void test() throws Exception {
    val id = 1L;

    getProfile(id).andExpect(status().isOk());

    updateProfile(id).andExpect(status().isOk());

    getStudentByLocation().andExpect(jsonPath("$[0]").value(id));
  }

  private ResultActions getStudentByLocation() throws Exception {
    return mockMVC.perform(
        get("/students")
            .param("lat", String.valueOf(location.getLatitude() + 0.01))
            .param("lng", String.valueOf(location.getLongitude() - 0.01)));
  }

  private ResultActions getProfile(Long id) throws Exception {
    return mockMVC.perform(get("/students/" + id + "/profile"));
  }

  private ResultActions updateProfile(Long id) throws Exception {
    val request = new UpdateProfileRequest("John Silver", "Hello, I'm John.", "PIRATE", null, location);

    return mockMVC.perform(
        put("/students/" + id + "/profile")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)));
  }

}
