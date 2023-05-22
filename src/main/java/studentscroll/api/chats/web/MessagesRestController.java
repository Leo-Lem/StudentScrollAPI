package studentscroll.api.chats.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import studentscroll.api.account.data.Student;
import studentscroll.api.chats.services.MessageService;
import studentscroll.api.chats.web.dto.MessageResponse;
import studentscroll.api.shared.ForbiddenException;
import studentscroll.api.shared.NotAuthenticatedException;

@Tag(name = "Messages", description = "Everything related to chat messages.")
@SecurityRequirement(name = "token")
@RestController
@RequestMapping("/chats/{chatId}/messages")
public class MessagesRestController {
  @Autowired
  private MessageService service;

  @Operation(summary = "Create a new message.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Created the message."),
      @ApiResponse(responseCode = "404", description = "Sender does not exist.", content = @Content) })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public MessageResponse create(
      @PathVariable Long chatId,
      @RequestBody String content,
      HttpServletResponse response)
      throws EntityNotFoundException, NotAuthenticatedException, ForbiddenException {
    val message = service.create(getCurrentStudent(), content, chatId);

    response.setHeader("Location", "/chats/" + chatId + "/messages/" + message.getId());

    return new MessageResponse(message);
  }

  @Operation(summary = "Find the message.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the message."),
      @ApiResponse(responseCode = "404", description = "Sender or receiver does not exist.", content = @Content) })
  @Parameter(in = ParameterIn.PATH, name = "chatId", required = true)
  @GetMapping("/{messageId}")
  public MessageResponse read(@PathVariable Long messageId) throws EntityNotFoundException {
    return new MessageResponse(service.read(messageId));
  }

  @Operation(summary = "Update the message.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Updated the message."),
      @ApiResponse(responseCode = "404", description = "Message does not exist.", content = @Content) })
  @Parameter(in = ParameterIn.PATH, name = "chatId", required = true)
  @PutMapping("/{messageId}")
  public MessageResponse update(
      @PathVariable Long messageId, @RequestBody String newContent)
      throws EntityNotFoundException {
    return new MessageResponse(service.update(messageId, newContent));
  }

  @Operation(summary = "Delete the message.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Deleted the message.", content = @Content),
      @ApiResponse(responseCode = "404", description = "Message does not exist.", content = @Content) })
  @Parameter(in = ParameterIn.PATH, name = "chatId", required = true)
  @DeleteMapping("/{messageId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
      @PathVariable Long messageId) throws EntityNotFoundException {
    service.delete(messageId);
  }

  private Student getCurrentStudent() throws NotAuthenticatedException {
    val student = (Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (student == null)
      throw new NotAuthenticatedException("You are not logged in.");

    return student;
  }

}
