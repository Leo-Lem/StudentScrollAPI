package studentscroll.api.utils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.val;
import studentscroll.api.account.web.dto.AuthenticationRequest;

@Component
@DirtiesContext
public class ITestUtils {

  @Autowired
  private MockMvc mockMVC;

  @Autowired
  private ObjectMapper objectMapper;

  public Set<Long> createStudents(Integer count) throws Exception {
    val ids = new HashSet<Long>();

    for (int i = 0; i < count; i++)
      ids.add(createStudent());

    return ids;
  }

  public Long createStudent() throws Exception {
    return createStudent(new Random().nextInt() + "@abc.com");
  }

  public Long createStudent(String email) throws Exception {
    val request = new AuthenticationRequest("John Silver", email, "1234");

    return objectMapper.readTree(
        mockMVC.perform(
            post("/account")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(jsonPath("$.id").isNumber()).andReturn().getResponse()
            .getContentAsString())
        .get("id").asLong();
  }

}
