package studentscroll.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.val;
import studentscroll.api.profiles.web.dto.UpdateProfileRequest;
import studentscroll.api.shared.StudentLocation;
import studentscroll.api.utils.ITestUtils;
import studentscroll.api.utils.TestUtils;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext
public class ProfileITest {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ITestUtils utils;

  @Test
  public void test() throws Exception {
    val studentId = utils.createStudent();
    val student = TestUtils.getStudent(studentId);

    getProfile(studentId).andExpect(status().isOk());

    TestUtils.authenticate(student);
    updateProfile().andExpect(status().isOk());

    getProfilesByName("John")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].studentId").value(studentId));

    getProfilesByInterests("PIRATE")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].studentId").value(studentId));
  }

  private ResultActions getProfilesByName(String name) throws Exception {
    return mockMVC.perform(get("/students?name=" + name));
  }

  private ResultActions getProfilesByInterests(String... interests) throws Exception {
    return mockMVC.perform(get("/students?interests=" + String.join(",", interests)));
  }

  private ResultActions getProfile(Long id) throws Exception {
    return mockMVC.perform(get("/students/" + id));
  }

  private ResultActions updateProfile() throws Exception {
    val request = new UpdateProfileRequest(
        "John Silver", "Hello, I'm John.", "PIRATE", List.of("PIRATE", "SWORD_FIGHTING"),
        new StudentLocation(0.0, 0.0));

    return mockMVC.perform(
        put("/students")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(request)));
  }

}
