package studentscroll.api.posts.web;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
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
      @ApiResponse(responseCode = "201", description = "Created the post."),
      @ApiResponse(responseCode = "400", description = "Missing fields in request.", content = @Content) })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PostResponse create(
      @RequestBody CreatePostRequest request, HttpServletResponse response) {
    val type = request.getType();

    if (type == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot resolve to post type");

    PostResponse postResponse;

    if (type.equals(EventPost.class))
      postResponse = new PostResponse(service.create(
          request.getPosterId(),
          request.getTitle(),
          Set.of(request.getTags()),
          request.getDescription(),
          request.getDate(),
          request.getLocation()));
    else
      postResponse = new PostResponse(service.create(
          request.getPosterId(),
          request.getTitle(),
          Set.of(request.getTags()),
          request.getContent()));

    response.setHeader("Location", "/posts/" + postResponse.getId());
    return postResponse;
  }

  @Operation(summary = "Find the post.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the post."),
      @ApiResponse(responseCode = "404", description = "Post does not exist.", content = @Content) })
  @GetMapping("/{postId}")
  public PostResponse read(
      @PathVariable Long postId) throws EntityNotFoundException {
    return new PostResponse(service.read(postId));
  }

  @Operation(summary = "Update the post.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Updated the post."),
      @ApiResponse(responseCode = "404", description = "Post does not exist.", content = @Content) })
  @PutMapping("/{postId}")
  public PostResponse update(
      @PathVariable Long postId, @RequestBody UpdatePostRequest request) throws EntityNotFoundException {
    return new PostResponse(service.update(postId,
        Optional.ofNullable(request.getNewTitle()),
        Optional.ofNullable(request.getNewTags()).map(Set::of),
        Optional.ofNullable(request.getNewDescription()),
        Optional.ofNullable(request.getNewDate()),
        Optional.ofNullable(request.getNewLocation()),
        Optional.ofNullable(request.getNewContent())));
  }

  @Operation(summary = "Delete the post.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Deleted the post.", content = @Content),
      @ApiResponse(responseCode = "404", description = "Post does not exist.", content = @Content) })
  @DeleteMapping("/{postId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
      @PathVariable Long postId) throws EntityNotFoundException {
    service.delete(postId);
  }

}
