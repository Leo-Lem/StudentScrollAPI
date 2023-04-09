package studentscroll.api.posts.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import io.swagger.v3.oas.annotations.media.*;
import lombok.val;
import studentscroll.api.posts.data.*;
import studentscroll.api.posts.services.*;
import studentscroll.api.posts.web.dto.*;

@Tag(name = "Posts", description = "Everything related to posts.")
@SecurityRequirement(name = "token")
@RestController
@RequestMapping("/posts")
public class PostsRestController {

  @Autowired
  private PostService service;

  @Operation(summary = "Create a new post.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Created the post.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))),
      @ApiResponse(responseCode = "400", description = "Missing fields in request.", content = @Content) })
  @PostMapping
  public ResponseEntity<?> create(@RequestBody CreatePostRequest request) {
    val type = request.getType();

    if (type == null)
      return ResponseEntity.badRequest().body("Cannot resolve to post type");

    PostResponse response;

    if (type.equals(EventPost.class))
      response = new PostResponse(service.create(
          request.getPosterId(), request.getTitle(), Set.of(request.getTags()),
          request.getDescription(), request.getDate(), request.getLocation()));
    else // if (type.equals(ContentPost.class))
      response = new PostResponse(service.create(
          request.getPosterId(), request.getTitle(), Set.of(request.getTags()), request.getContent()));

    try {
      return ResponseEntity.created(new URI("/posts/" + response.getId())).body(response);
    } catch (URISyntaxException e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }

  @Operation(summary = "Find the post.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the post.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))),
      @ApiResponse(responseCode = "404", description = "Post does not exist.", content = @Content) })
  @GetMapping("/{postId}")
  public ResponseEntity<?> read(@PathVariable Long postId) {
    try {
      return ResponseEntity.ok(new PostResponse(service.read(postId)));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Update the post.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Updated the post.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class))),
      @ApiResponse(responseCode = "404", description = "Post does not exist.", content = @Content) })
  @PutMapping("/{postId}")
  public ResponseEntity<?> update(@PathVariable Long postId, @RequestBody UpdatePostRequest request) {
    try {
      return ResponseEntity.ok(
          new PostResponse(service.update(postId,
              Optional.ofNullable(request.getNewTitle()),
              Optional.ofNullable(request.getNewTags()).map(Set::of),
              Optional.ofNullable(request.getNewDescription()),
              Optional.ofNullable(request.getNewDate()),
              Optional.ofNullable(request.getNewLocation()),
              Optional.ofNullable(request.getNewContent()))));
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Delete the post.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Deleted the post.", content = @Content),
      @ApiResponse(responseCode = "404", description = "Post does not exist.", content = @Content) })
  @DeleteMapping("/{postId}")
  public ResponseEntity<?> delete(@PathVariable Long postId) {
    try {
      service.delete(postId);
      return ResponseEntity.noContent().build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    }
  }

}
