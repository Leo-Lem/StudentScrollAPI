package studentscroll.api.chats.web;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import studentscroll.api.chats.services.ChatService;
import studentscroll.api.chats.web.dto.ChatResponse;
import studentscroll.api.chats.web.dto.CreateChatRequest;

@Tag(name = "Chats", description = "Everything related to chats.")
@RestController
@RequestMapping("/chats")
public class ChatsRestController {
  @Autowired
  private ChatService service;

  @Operation(summary = "Create a new chat.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Created the chat."),
      @ApiResponse(responseCode = "404", description = "A participant does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ChatResponse create(
      @RequestBody CreateChatRequest request,
      HttpServletResponse response) throws EntityNotFoundException {
    val chat = service.create(request.getParticipantIds());

    response.setHeader("Location", "/chats/" + chat.getId());

    return new ChatResponse(chat);
  }

  @Operation(summary = "Find the chat.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the chat."),
      @ApiResponse(responseCode = "404", description = "Chat does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @GetMapping("/{chatId}")
  public ChatResponse read(@PathVariable Long chatId) throws EntityNotFoundException {
    return new ChatResponse(service.read(chatId));
  }

  @Operation(summary = "Delete the chat.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Deleted the chat.", content = @Content),
      @ApiResponse(responseCode = "404", description = "Chat does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @DeleteMapping("/{chatId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
      @PathVariable Long chatId) throws EntityNotFoundException {
    service.delete(chatId);
  }

  @Operation(summary = "Find the chats.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the chats."),
      @ApiResponse(responseCode = "404", description = "Participant does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @GetMapping
  public List<ChatResponse> readByParticipantId(@RequestParam Long participantId) throws EntityNotFoundException {
    return service.readByParticipantId(participantId).stream().map(ChatResponse::new).collect(Collectors.toList());
  }
}
