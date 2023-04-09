package studentscroll.api.posts.web;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.val;
import studentscroll.api.posts.data.*;
import studentscroll.api.posts.services.*;
import studentscroll.api.posts.web.dto.*;

@RestController
@RequestMapping("/posts")
public class PostsRestController {

  @Autowired
  private PostService postService;

  @Autowired
  private EventPostService eventPostService;

  @Autowired
  private ContentPostService contentPostService;

  @PostMapping
  public ResponseEntity<?> create(@RequestBody CreatePostRequest request) {
    val type = request.getType();

    if (type == null)
      return ResponseEntity.badRequest().body("Cannot resolve to post type");

    if (type.equals(EventPost.class))
      return ResponseEntity.ok(
          new PostResponse(
              eventPostService.create(
                  request.getPosterId(), request.getTitle(), request.getDescription(),
                  request.getDate(), request.getLocation(), Set.of(request.getTags()))));
    else if (type.equals(ContentPost.class))
      return ResponseEntity.ok(
          new PostResponse(
              contentPostService.create(
                  request.getPosterId(), request.getTitle(), request.getContent(), Set.of(request.getTags()))));

    return ResponseEntity.internalServerError().build();
  }

  @GetMapping("/{postId}")
  public ResponseEntity<?> read(@PathVariable Long postId) {
    return ResponseEntity.internalServerError().body("Unimplemented");
  }

  @PutMapping("/{postId}")
  public ResponseEntity<?> update(@PathVariable Long postId, @RequestBody UpdatePostRequest request) {
    return ResponseEntity.internalServerError().body("Unimplemented");
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<?> delete(@PathVariable Long postId) {
    return ResponseEntity.internalServerError().body("Unimplemented");
  }

}
