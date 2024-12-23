package techquack.com.onestar3gram.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techquack.com.onestar3gram.DTO.PostDTO;
import techquack.com.onestar3gram.DTO.PublicationDetail;
import techquack.com.onestar3gram.DTO.EditPostCommand;
import techquack.com.onestar3gram.config.KeycloakRoles;
import techquack.com.onestar3gram.entities.MediaFile;
import techquack.com.onestar3gram.entities.Post;
import techquack.com.onestar3gram.exceptions.InvalidDescriptionException;
import techquack.com.onestar3gram.exceptions.PostNotFoundException;
import techquack.com.onestar3gram.exceptions.UnauthorizedPostException;
import techquack.com.onestar3gram.services.PostService;

import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final StorageService storageService;

    public PostController(PostService postService, StorageService storageService) {
        this.postService = postService;
        this.storageService = storageService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public @ResponseBody PostDTO getPost(@PathVariable(value = "id") Integer postId) throws PostNotFoundException, UnauthorizedPostException {
        Post post = postService.getPost(postId);
        if (post.isPrivate() && !KeycloakRoles.hasRole(KeycloakRoles.ADMIN) && !KeycloakRoles.hasRole(KeycloakRoles.PRIVILEGED)) {
            throw new UnauthorizedPostException("error - impossible to see post");
        }
        else return postService.getDTO(post);
    }

    @GetMapping(value = "", produces = "application/json")
    public @ResponseBody List<PostDTO> getPosts() {
        boolean canUserSeePrivatePosts = KeycloakRoles.hasRole(KeycloakRoles.ADMIN) || KeycloakRoles.hasRole(KeycloakRoles.PRIVILEGED);
        if (canUserSeePrivatePosts) {
            return postService.getDTOList(postService.getAllPosts());
        }
        return postService.getDTOList(postService.getPublicPosts());
    }

    @PostMapping(value = "", produces = "application/json")
    public @ResponseBody int sendPost(@RequestBody SendPostCommand sendPostCommand,
                                      @AuthenticationPrincipal Jwt jwt) throws InvalidDescriptionException, FileNotFoundException, InvalidAltException {

        MediaFile media = storageService.getMediaFile(sendPostCommand.getMediaId());
        String alt = sendPostCommand.getAlt();
        String description = sendPostCommand.getDescription();
        boolean visibility = sendPostCommand.getVisibility();
        if (postService.isDescriptionInvalid(description)) {
            throw new InvalidDescriptionException("Too Long Text - must be less than 500 characters");
        }
        if (postService.isAltInvalid(alt)) {
            throw new InvalidAltException("Too long text - must be less than 200 characters");
        }
        MediaFile media = publicationDetail.getMedia();
        String creatorId = jwt.getSubject();
        return postService.createPost(media, alt, description, visibility, creatorId);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public @ResponseBody Post editPost(@PathVariable(value = "id") int postId, @RequestBody EditPostCommand editPostCommand) throws InvalidDescriptionException, PostNotFoundException, InvalidAltException {
        String alt = editPostCommand.getAlt();
        String description = editPostCommand.getDescription();
        Boolean visibility = editPostCommand.getVisibility();
        if (postService.isDescriptionInvalid(description)) {
            throw new InvalidDescriptionException("Too long text - must be less than 500 characters");
        }
        if (postService.isAltInvalid(alt)) {
            throw new InvalidAltException("Too long text - must be less than 200 characters");
        }
        return postService.updatePost(postId, alt, description, visibility);
    }

    @DeleteMapping(value = "/{id}", produces = "application/json")
    public @ResponseBody void deletePost(@PathVariable(value = "id") int postId) {
        postService.deletePost(postId);
    }

    @PutMapping(value = "/like/add/{id}", produces = "application/json")
    public @ResponseBody Post likePost(@PathVariable(value = "id") int postId, @AuthenticationPrincipal Jwt jwt) throws PostNotFoundException {
        return postService.addLike(postId, jwt.getSubject());
    }

    @PutMapping(value = "/like/remove/{id}", produces = "application/json")
    public @ResponseBody Post unlikePost(@PathVariable(value = "id") int postId, @AuthenticationPrincipal Jwt jwt) throws PostNotFoundException, NegativeLikeNumberException {
        return postService.removeLike(postId, jwt.getSubject());
    }

}
