package studentscroll.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.*;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(version = "1.0.0", title = "Student Scroll REST API", description = "This is the REST API for Student Scroll.", license = @License(name = "MIT License", url = "https://github.com/Leo-Lem/StudentScrollAPI/blob/main/LICENSE")), servers = {
    @Server(url = "https://studentscroll.net/api/v1", description = "production"),
    @Server(url = "http://localhost:8080", description = "development") })
@SpringBootApplication
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

}