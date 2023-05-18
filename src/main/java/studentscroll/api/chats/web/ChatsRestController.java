package studentscroll.api.chats.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import studentscroll.api.chats.services.MessageService;
import studentscroll.api.chats.web.dto.*;

@Tag(name = "Chats", description = "Everything related to chats.")
@RestController
@RequestMapping("/chats")
public class ChatsRestController {
 

  // @Operation(summary = "Find the chats.")
  // @ApiResponses(value = {
  // @ApiResponse(responseCode = "200", description = "Found the chats."),
  // @ApiResponse(responseCode = "404", description = "Chats doesn't exist.",
  // content = @Content) })
  // @SecurityRequirement(name = "token")
  // @GetMapping("/{chatId}")
  // public MessageResponse getChats(@PathVariable Long id) throws
  // EntityNotFoundException {
  // return new ChatResponse(service.read(id));
  // }
}
