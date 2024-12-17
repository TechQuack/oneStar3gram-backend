package techquack.com.onestar3gram.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techquack.com.onestar3gram.entities.AppUser;
import techquack.com.onestar3gram.entities.MediaFile;
import techquack.com.onestar3gram.entities.Post;
import techquack.com.onestar3gram.exceptions.InvalidDescriptionException;
import techquack.com.onestar3gram.exceptions.NegativeLikeNumberException;
import techquack.com.onestar3gram.exceptions.PostNotFoundException;
import techquack.com.onestar3gram.services.PostService;
import techquack.com.onestar3gram.DTO.PublicationDetail;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public @ResponseBody Post getPost(@PathVariable(value = "id") Integer postId) throws PostNotFoundException {
        return postService.getPost(postId);
    }

    @GetMapping(value = "", produces = "application/json")
    public @ResponseBody List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping(value = "/accessible", produces = "application/json")
    public @ResponseBody List<Post> getAccessiblePosts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String adminRole = "";
        String privilegeRole = "";
        //TODO: add roles names
        boolean canUserSeePrivatePosts = auth.getAuthorities().stream().anyMatch(g -> g.getAuthority().equals(adminRole))
                || auth.getAuthorities().stream().anyMatch(g -> g.getAuthority().equals(privilegeRole));
        if (canUserSeePrivatePosts) {
            return postService.getAllPosts();
        }
        return postService.getPublicPosts();
    }

    @PostMapping(value = "/send", produces = "application/json")
    public @ResponseBody Integer sendPost(@RequestBody PublicationDetail publicationDetail, @RequestParam("alt") String alt,
                                          @RequestParam("description") String description,
                                          @RequestParam("visibility") boolean visibility) throws InvalidDescriptionException {
        if (postService.isDescriptionInvalid(description)) {
            throw new InvalidDescriptionException("Too Long Text - must be less than 500 characters");
        }
        MediaFile media = publicationDetail.getMedia();
        AppUser creator = publicationDetail.getCreator();
        return postService.createPost(media, alt, description, visibility, creator);
    }

    @PutMapping(value = "/edit/{id}", produces = "application/json")
    public @ResponseBody Post editPost(@PathVariable(value = "id") Integer postId, @RequestParam("alt") String alt, @RequestParam("description") String description, @RequestParam("visibility") boolean visibility) throws InvalidDescriptionException, PostNotFoundException {
        if (postService.isDescriptionInvalid(description)) {
            throw new InvalidDescriptionException("Too Long Text - must be less than 500 characters");
        }
        return postService.updatePost(postId, alt, description, visibility);
    }

    @DeleteMapping(value = "/delete/{id}", produces = "application/json")
    public @ResponseBody void deletePost(@PathVariable(value = "id") Integer postId) {
        postService.deletePost(postId);
    }

    @PutMapping(value = "/like/add/{id}", produces = "application/json")
    public @ResponseBody Post likePost(@PathVariable(value = "id") Integer postId) throws PostNotFoundException {
        return postService.addLike(postId);
    }

    @PutMapping(value = "/like/remove/{id}", produces = "application/json")
    public @ResponseBody Post unlikePost(@PathVariable(value = "id") Integer postId) throws PostNotFoundException, NegativeLikeNumberException {
        return postService.removeLike(postId);
    }

}
