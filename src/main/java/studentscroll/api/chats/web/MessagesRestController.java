package studentscroll.api.chats.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import studentscroll.api.chats.services.MessageService;
import studentscroll.api.chats.web.dto.*;

@Tag(name = "Messages", description = "Everything related to chat messages.")
@RestController
@RequestMapping("/chats/{chatId}/messages")
public class MessagesRestController {
  @Autowired
  private MessageService service;

  @Operation(summary = "Create a new message.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Created the message."),
      @ApiResponse(responseCode = "404", description = "Sender or receiver does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public MessageResponse create(
      @PathVariable Long chatId,
      @RequestBody CreateMessageRequest request,
      HttpServletResponse response)
      throws EntityNotFoundException {

    val message = service.create(request.getContent(), request.getSenderId(), chatId);

    response.setHeader("Location", "/chats/" + chatId + "/messages/" + message.getId());

    return new MessageResponse(message);
  }

  @Operation(summary = "Find the message.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the message."),
      @ApiResponse(responseCode = "404", description = "Sender or receiver does not exist.", content = @Content) })
  @Parameter(in = ParameterIn.PATH, name = "chatId", required = true)
  @SecurityRequirement(name = "token")
  @GetMapping("/{messageId}")
  public MessageResponse read(@PathVariable Long messageId) throws EntityNotFoundException {
    return new MessageResponse(service.read(messageId));
  }

  @Operation(summary = "Update the message.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Updated the message."),
      @ApiResponse(responseCode = "404", description = "Message does not exist.", content = @Content) })
  @Parameter(in = ParameterIn.PATH, name = "chatId", required = true)
  @SecurityRequirement(name = "token")
  @PutMapping("/{messageId}")
  public MessageResponse update(
      @PathVariable Long messageId, @RequestBody String newContent) throws EntityNotFoundException {
    return new MessageResponse(service.update(messageId, newContent));
  }

  @Operation(summary = "Delete the message.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Deleted the message.", content = @Content),
      @ApiResponse(responseCode = "404", description = "Message does not exist.", content = @Content) })
  @Parameter(in = ParameterIn.PATH, name = "chatId", required = true)
  @SecurityRequirement(name = "token")
  @DeleteMapping("/{messageId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
      @PathVariable Long messageId) throws EntityNotFoundException {
    service.delete(messageId);
  }
}
