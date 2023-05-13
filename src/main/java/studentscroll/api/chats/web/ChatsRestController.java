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
import studentscroll.api.chats.services.ChatService;
import studentscroll.api.chats.web.dto.*;

@Tag(name = "Messages", description = "Everything related to chat messages.")
@RestController
@RequestMapping("/chats")
public class ChatsRestController {
  @Autowired
  private ChatService service;

  @Operation(summary = "Create a new message.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Created the message."),
      @ApiResponse(responseCode = "404", description = "Sender or receiver does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public MessageResponse create(
      @RequestBody CreateMessageRequest request, HttpServletResponse response) throws EntityNotFoundException {

    val message = service.create(request.getContent(), request.getSenderId(), request.getReceiverId());

    response.setHeader("Location", "/chats/" + message.getId());

    return new MessageResponse(message);
  }

  @Operation(summary = "Find the message.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the message."),
      @ApiResponse(responseCode = "404", description = "Sender or receiver does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @GetMapping("/{chatId}")
  public MessageResponse read(@PathVariable Long id) throws EntityNotFoundException {
    return new MessageResponse(service.read(id));
  }

  @Operation(summary = "Update the message.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Updated the message."),
      @ApiResponse(responseCode = "404", description = "Message does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @PutMapping("/{chatId}")
  public MessageResponse update(
      @PathVariable Long id, @RequestBody String newContent) throws EntityNotFoundException {
    var message = service.read(id);

    message = service.update(id, newContent);

    return new MessageResponse(message);
  }

  @Operation(summary = "Delete the message.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Deleted the message.", content = @Content),
      @ApiResponse(responseCode = "404", description = "Message does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @DeleteMapping("/{chatId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
      @PathVariable Long id) throws EntityNotFoundException {
    service.delete(id);
  }
}
