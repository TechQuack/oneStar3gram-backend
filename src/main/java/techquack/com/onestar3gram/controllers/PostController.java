package techquack.com.onestar3gram.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techquack.com.onestar3gram.DTO.UpdatePostCommand;
import techquack.com.onestar3gram.entities.AppUser;
import techquack.com.onestar3gram.entities.MediaFile;
import techquack.com.onestar3gram.entities.Post;
import techquack.com.onestar3gram.exceptions.*;
import techquack.com.onestar3gram.services.AppUserService;
import techquack.com.onestar3gram.services.PostService;
import techquack.com.onestar3gram.DTO.SendPostCommand;
import techquack.com.onestar3gram.services.storage.StorageService;

import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final AppUserService userService;
    private final StorageService storageService;

    public PostController(PostService postService, AppUserService userService, StorageService storageService) {
        this.postService = postService;
        this.userService = userService;
        this.storageService = storageService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public @ResponseBody Post getPost(@PathVariable(value = "id") int postId) throws PostNotFoundException {
        return postService.getPost(postId);
    }

    @GetMapping(value = "", produces = "application/json")
    public @ResponseBody List<Post> getPosts() {
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

    @PostMapping(value = "", produces = "application/json")
    public @ResponseBody int sendPost(@RequestBody SendPostCommand sendPostCommand) throws InvalidDescriptionException, FileNotFoundException {

        MediaFile media = storageService.getMediaFile(sendPostCommand.getMediaId());
        AppUser creator = userService.getUserById(sendPostCommand.getCreatorId());
        String alt = sendPostCommand.getAlt();
        String description = sendPostCommand.getDescription();
        boolean visibility = sendPostCommand.getVisibility();
        if (postService.isDescriptionInvalid(description)) {
            throw new InvalidDescriptionException("Too Long Text - must be less than 500 characters");
        }
        return postService.createPost(media, alt, description, visibility, creator);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public @ResponseBody Post editPost(@PathVariable(value = "id") int postId, @RequestBody UpdatePostCommand updatePostCommand) throws InvalidDescriptionException, PostNotFoundException, InvalidAltException {
        String alt = updatePostCommand.getAlt();
        String description = updatePostCommand.getDescription();
        Boolean visibility = updatePostCommand.getVisibility();
        if (postService.isDescriptionInvalid(description)) {
            throw new InvalidDescriptionException("Too long text - must be less than 500 characters");
        }
        if (postService.isAltInvalid(alt)) {
            throw new InvalidAltException("Too long text - must be less than 200 characters");
        }
        return postService.updatePost(postId, alt, description, visibility);
    }

    @DeleteMapping(value = "/delete/{id}", produces = "application/json")
    public @ResponseBody void deletePost(@PathVariable(value = "id") int postId) {
        postService.deletePost(postId);
    }

    @PutMapping(value = "/like/add/{id}", produces = "application/json")
    public @ResponseBody Post likePost(@PathVariable(value = "id") int postId) throws PostNotFoundException {
        return postService.addLike(postId);
    }

    @PutMapping(value = "/like/remove/{id}", produces = "application/json")
    public @ResponseBody Post unlikePost(@PathVariable(value = "id") int postId) throws PostNotFoundException, NegativeLikeNumberException {
        return postService.removeLike(postId);
    }

}
