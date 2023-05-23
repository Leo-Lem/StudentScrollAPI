package studentscroll.api.posts.web;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import io.swagger.v3.oas.annotations.media.*;
import lombok.val;
import studentscroll.api.account.data.Account;
import studentscroll.api.posts.data.*;
import studentscroll.api.posts.services.*;
import studentscroll.api.posts.web.dto.*;
import studentscroll.api.shared.NotAuthenticatedException;

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
      @ApiResponse(responseCode = "400", description = "Missing fields in request.", content = @Content),
      @ApiResponse(responseCode = "404", description = "Poster with id does not exist.", content = @Content) })
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PostResponse create(
      @RequestBody CreatePostRequest request, HttpServletResponse response) throws NotAuthenticatedException {
    val posterId = getCurrentStudent().getId();
    val type = request.getType();

    if (type == null)
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot resolve to post type");

    PostResponse postResponse;

    if (type.equals(EventPost.class))
      postResponse = new PostResponse(service.create(
          posterId,
          request.getTitle(),
          Set.of(request.getTags()),
          request.getDescription(),
          request.getDate(),
          request.getLocation()));
    else
      postResponse = new PostResponse(service.create(
          posterId,
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

  @Operation(summary = "Find all matching posts.")
  @ApiResponse(responseCode = "200", description = "Returning posts.")
  @GetMapping
  public List<PostResponse> readAll(
      HttpServletResponse response,
      @RequestParam Optional<List<Long>> posterIds,
      @RequestParam Optional<Integer> page,
      @RequestParam Optional<Integer> size,
      @RequestParam Optional<List<String>> sort,
      @RequestParam Optional<Boolean> sortAscending) throws NotAuthenticatedException {

    val pageable = service.createPageable(page, size, sort, sortAscending);

    Page<? extends Post> posts;

    if (posterIds.isPresent())
      posts = service.readAllByPosterIds(posterIds.get(), pageable);
    else
      posts = service.readAll(getCurrentStudent().getProfile(), pageable);

    response.setHeader("X-Total-Count", String.valueOf(posts.getTotalElements()));

    response.addHeader("Link", buildPageLinkHeader("first", 0, posts));
    response.addHeader("Link", buildPageLinkHeader("last", posts.getTotalPages(), posts));

    if (posts.hasNext())
      response.addHeader("Link", buildPageLinkHeader("next", posts.getNumber() + 1, posts));
    if (posts.hasPrevious())
      response.addHeader("Link", buildPageLinkHeader("previous", posts.getNumber() - 1, posts));

    return posts.stream().map(PostResponse::new).toList();
  }

  private String buildPageLinkHeader(String rel, Integer page, Page<? extends Post> posts) {
    val builder = ServletUriComponentsBuilder.fromCurrentRequest();
    builder.replaceQueryParam("page", page);
    builder.replaceQueryParam("size", posts.getSize());
    return "<" + builder.toUriString() + ">; rel=\"" + rel + "\"";
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

  private Account getCurrentStudent() throws NotAuthenticatedException {
    val student = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (student == null)
      throw new NotAuthenticatedException("You are not logged in.");

    return student;
  }

}
