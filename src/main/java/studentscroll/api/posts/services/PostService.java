package studentscroll.api.posts.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.val;
import studentscroll.api.account.data.AccountRepository;
import studentscroll.api.posts.data.ContentPost;
import studentscroll.api.posts.data.EventPost;
import studentscroll.api.posts.data.Post;
import studentscroll.api.posts.data.PostRepository;
import studentscroll.api.profiles.data.Profile;
import studentscroll.api.shared.StudentLocation;

@Service
public class PostService {

  @Autowired
  protected PostRepository repo;

  @Autowired
  private AccountRepository studentRepo;

  public EventPost create(
      @NonNull Long posterId,
      @NonNull String title,
      @NonNull Set<String> tags,
      @NonNull String description,
      @NonNull LocalDateTime date,
      @NonNull StudentLocation location) throws EntityNotFoundException {
    return (EventPost) create(posterId, new EventPost(title, tags, description, date, location));
  }

  public ContentPost create(
      @NonNull Long posterId,
      @NonNull String title,
      @NonNull Set<String> tags,
      @NonNull String content) throws EntityNotFoundException {
    return (ContentPost) create(posterId, new ContentPost(title, tags, content));
  }

  public Post read(@NonNull Long postID) throws EntityNotFoundException {
    return repo.findById(postID).orElseThrow(() -> new EntityNotFoundException());
  }

  public Page<Post> readAll(
      @NonNull Profile profile,
      @NonNull Pageable pageable) {
    val followIds = profile.getFollows().stream().map(f -> f.getId()).toList();
    val posterIds = Stream.concat(followIds.stream(), Stream.of(profile.getId())).toList();

    val followed = repo.findByPosterIdIn(posterIds, pageable);

    if (pageable.isUnpaged()) {
      val remaining = repo.findByPosterIdNotIn(posterIds, pageable);
      return new PageImpl<>(Stream.concat(
          followed.stream(),
          remaining.stream()).toList());
    }

    val remainingPageIndex = pageable.getPageNumber() - followed.getTotalPages();

    if (remainingPageIndex >= 0) {
      val remaining = repo.findByPosterIdNotIn(
          posterIds, PageRequest.of(remainingPageIndex, pageable.getPageSize(), pageable.getSort()));

      return new PageImpl<>(
          Stream.concat(followed.stream(), remaining.stream()).toList(),
          pageable,
          followed.getTotalElements() + remaining.getTotalElements());
    }

    return followed;
  }

  public Page<Post> readAllByPosterIds(@NonNull List<Long> posterIds, @NonNull Pageable pageable) {
    return repo.findByPosterIdIn(posterIds, pageable);
  }

  public Page<Post> readAllByTitle(@NonNull String title, @NonNull Pageable pageable) {
    return repo.findByTitle(title, pageable);
  }

  public Page<Post> readAllByTag(@NonNull String tag, @NonNull Pageable pageable) {
    return repo.findByTags(tag, pageable);
  }

  public Page<ContentPost> readByContent(@NonNull String pattern, @NonNull Pageable pageable) {
    return repo.findByContentLike(pattern, pageable);
  }

  public Pageable createPageable(
      @NonNull Optional<Integer> page,
      @NonNull Optional<Integer> size,
      @NonNull Optional<List<String>> sort,
      @NonNull Optional<Boolean> sortAscending) {
    val sortOrder = sort
        .map(s -> Sort.by(sortAscending.orElse(true) ? Sort.Direction.ASC : Sort.Direction.DESC,
            s.toArray(new String[0])))
        .orElse(Sort.unsorted());

    return page.map(p -> (Pageable) PageRequest.of(p, size.orElse(10), sortOrder)).orElse(Pageable.unpaged());
  }

  public Post update(
      @NonNull Long postID,
      @NonNull Optional<String> newTitle,
      @NonNull Optional<Set<String>> newTags,
      @NonNull Optional<String> newDescription,
      @NonNull Optional<LocalDateTime> newDate,
      @NonNull Optional<StudentLocation> newLocation,
      @NonNull Optional<String> newContent) throws EntityNotFoundException {
    try {
      final Post post = read(postID);

      newTitle.ifPresent(unwrapped -> post.setTitle(unwrapped));
      newTags.ifPresent(unwrapped -> post.setTags(unwrapped));

      if (post instanceof EventPost) {
        EventPost eventPost = (EventPost) post;
        newDescription.ifPresent(unwrapped -> eventPost.setDescription(unwrapped));
        newDate.ifPresent(unwrapped -> eventPost.setDate(unwrapped));
        newLocation.ifPresent(unwrapped -> eventPost.setLocation(unwrapped));
        return repo.save(eventPost);
      } else if (post instanceof ContentPost) {
        ContentPost contentPost = (ContentPost) post;
        newContent.ifPresent(unwrapped -> contentPost.setContent(unwrapped));
        return repo.save(contentPost);
      } else
        return repo.save(post);
    } catch (ClassCastException e) {
      throw new EntityNotFoundException("Updated post is of wrong type.");
    }
  }

  public void delete(
      @NonNull Long postId) throws EntityNotFoundException {
    val post = read(postId);
    studentRepo.save(post.getPoster().removePost(post));
  }

  private Post create(
      @NonNull Long posterId,
      @NonNull Post post) throws EntityNotFoundException {
    post.setPoster(studentRepo
        .findById(posterId)
        .orElseThrow(() -> new EntityNotFoundException("Student does not exist.")));

    return repo.save(post);
  }

}