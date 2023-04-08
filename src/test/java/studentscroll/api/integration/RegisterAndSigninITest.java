package studentscroll.api.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.val;
import studentscroll.api.security.auth.SigninRequest;
import studentscroll.api.students.web.requestDTO.CreateStudentRequest;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class RegisterAndSigninITest {

        @Autowired
        private MockMvc mockMVC;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void whenSigningUp_thenUserIsCreatedInDatabaseAndResponseIs200AndCannotSignUpAgain()
                        throws Exception {
                String email = "jwayne@xyz.com", password = "1234";
                val registerRequest = new CreateStudentRequest("John Wayne", email, password);

                mockMVC.perform(
                                post("/students")
                                                .contentType("application/json")
                                                .content(objectMapper.writeValueAsString(registerRequest)))
                                .andExpect(status().isOk());

                mockMVC.perform(
                                post("/students")
                                                .contentType("application/json")
                                                .content(objectMapper.writeValueAsString(registerRequest)))
                                .andExpect(status().isConflict());

                val signinRequest = new SigninRequest(email, password);

                mockMVC.perform(
                                post("/signin")
                                                .contentType("application/json")
                                                .content(objectMapper.writeValueAsString(signinRequest)))
                                .andExpect(status().isOk());
        }

}
