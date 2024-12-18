package techquack.com.onestar3gram.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techquack.com.onestar3gram.config.KeycloakRoles;
import techquack.com.onestar3gram.entities.AppUser;
import techquack.com.onestar3gram.entities.MediaFile;
import techquack.com.onestar3gram.entities.Post;
import techquack.com.onestar3gram.exceptions.InvalidDescriptionException;
import techquack.com.onestar3gram.exceptions.NegativeLikeNumberException;
import techquack.com.onestar3gram.exceptions.PostNotFoundException;
import techquack.com.onestar3gram.exceptions.UnauthorizedPostException;
import techquack.com.onestar3gram.services.PostService;
import techquack.com.onestar3gram.DTO.PublicationDetail;

import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public @ResponseBody Post getPost(@PathVariable(value = "id") Integer postId) throws PostNotFoundException, UnauthorizedPostException {
        Post post = postService.getPost(postId);
        if (post.isPrivate() && !KeycloakRoles.hasRole(KeycloakRoles.ADMIN) && !KeycloakRoles.hasRole(KeycloakRoles.PRIVILEGED)) {
            throw new UnauthorizedPostException("error - impossible to see post");
        }
        else return post;
    }

    @GetMapping(value = "", produces = "application/json")
    public @ResponseBody List<Post> getPosts() {
        boolean canUserSeePrivatePosts = KeycloakRoles.hasRole(KeycloakRoles.ADMIN) || KeycloakRoles.hasRole(KeycloakRoles.PRIVILEGED);
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
