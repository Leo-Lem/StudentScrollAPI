package studentscroll.api.chats.web;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import studentscroll.api.account.data.Student;
import studentscroll.api.chats.services.ChatService;
import studentscroll.api.chats.web.dto.ChatResponse;
import studentscroll.api.shared.NotAuthenticatedException;

@Tag(name = "Chats", description = "Everything related to chats.")
@SecurityRequirement(name = "token")
@RestController
@RequestMapping("/chats")
public class ChatsRestController {
  @Autowired
  private ChatService service;

  @Operation(summary = "Create a new chat.", description = "The current student is automatically added to the chat.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Created the chat."),
      @ApiResponse(responseCode = "404", description = "A participant does not exist.", content = @Content) })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ChatResponse create(
      @RequestBody Set<Long> participantIds,
      HttpServletResponse response) throws EntityNotFoundException, NotAuthenticatedException {
    val chat = service.create(getCurrentStudent().getId(), participantIds);

    response.setHeader("Location", "/chats/" + chat.getId());

    return new ChatResponse(chat);
  }

  @Operation(summary = "Find the chat.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the chat."),
      @ApiResponse(responseCode = "404", description = "Chat does not exist.", content = @Content) })
  @GetMapping("/{chatId}")
  public ChatResponse read(@PathVariable Long chatId)
      throws EntityNotFoundException {
    return new ChatResponse(service.read(chatId));
  }

  @Operation(summary = "Find your chats.")
  @ApiResponse(responseCode = "200", description = "Found the chats.")
  @GetMapping
  public List<ChatResponse> readAll() throws EntityNotFoundException, NotAuthenticatedException {
    return service.readByStudentId(getCurrentStudent().getId())
        .stream().map(ChatResponse::new).collect(Collectors.toList());
  }

  @Operation(summary = "Delete the chat.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Deleted the chat.", content = @Content),
      @ApiResponse(responseCode = "404", description = "Chat does not exist.", content = @Content) })
  @DeleteMapping("/{chatId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
      @PathVariable Long chatId) throws EntityNotFoundException {
    service.delete(chatId);
  }

  private Student getCurrentStudent() throws NotAuthenticatedException {
    val student = (Student) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (student == null)
      throw new NotAuthenticatedException("You are not logged in.");

    return student;
  }

}
