package studentscroll.api.utils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Set;

import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.val;
import studentscroll.api.auth.dto.AuthenticationRequest;

@RequiredArgsConstructor
public class ITestUtils {

  public final MockMvc mockMVC;
  public final ObjectMapper objectMapper;

  public Set<Long> createStudents() throws Exception {
    return Set.of(
        createStudent("1@abc.com"),
        createStudent("2@abc.com"),
        createStudent("3@abc.com"));
  }

  public Long createStudent() throws Exception {
    return createStudent("abc@xyz.com");
  }

  public Long createStudent(String email) throws Exception {
    val request = new AuthenticationRequest("John Silver", email, "1234");

    return objectMapper.readTree(
        mockMVC.perform(
            post("/students")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(jsonPath("$.id").isNumber()).andReturn().getResponse()
            .getContentAsString())
        .get("id").asLong();
  }

}
