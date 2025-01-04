package techquack.com.onestar3gram.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.ws.rs.POST;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techquack.com.onestar3gram.DTO.CommentDTO;
import techquack.com.onestar3gram.DTO.PostDTO;
import techquack.com.onestar3gram.DTO.EditPostCommand;
import techquack.com.onestar3gram.DTO.SendPostCommand;
import techquack.com.onestar3gram.config.KeycloakRoles;
import techquack.com.onestar3gram.entities.MediaFile;
import techquack.com.onestar3gram.entities.Post;
import techquack.com.onestar3gram.exceptions.media.FileNotFoundException;
import techquack.com.onestar3gram.exceptions.post.InvalidAltException;
import techquack.com.onestar3gram.exceptions.post.InvalidDescriptionException;
import techquack.com.onestar3gram.exceptions.post.PostNotFoundException;
import techquack.com.onestar3gram.exceptions.post.UnauthorizedPostException;
import techquack.com.onestar3gram.services.PostService;
import techquack.com.onestar3gram.services.storage.StorageService;

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

    @Operation(summary = "Get a post by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the post",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized to get this post",
                    content = @Content)})
    @GetMapping(value = "/{id}", produces = "application/json")
    public @ResponseBody ResponseEntity<PostDTO> getPost(@PathVariable(value = "id") int postId) throws PostNotFoundException, UnauthorizedPostException {
        Post post = postService.getPost(postId);
        if (post.isPrivate() && !KeycloakRoles.hasRole(KeycloakRoles.ADMIN) && !KeycloakRoles.hasRole(KeycloakRoles.PRIVILEGED)) {
            throw new UnauthorizedPostException("error - impossible to see post");
        }
        else return ResponseEntity.status(HttpStatus.OK).body(postService.getDTO(post));
    }

    @Operation(summary = "Get posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the posts",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PostDTO.class))) }),
    })
    @GetMapping(value = "", produces = "application/json")
    public @ResponseBody ResponseEntity<List<PostDTO>> getPosts() {
        boolean canUserSeePrivatePosts = KeycloakRoles.hasRole(KeycloakRoles.ADMIN) || KeycloakRoles.hasRole(KeycloakRoles.PRIVILEGED);
        if (canUserSeePrivatePosts) {
            return ResponseEntity.status(HttpStatus.OK).body(postService.getDTOList(postService.getAllPosts()));
        }
        return ResponseEntity.status(HttpStatus.OK).body(postService.getDTOList(postService.getPublicPosts()));
    }

    @Operation(summary = "Get posts by author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the posts with the author username",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PostDTO.class))) }),
            @ApiResponse(responseCode = "400", description = "Invalid post id",
                    content = @Content) })
    @GetMapping(value = "author/{username}", produces = "application/json")
    public @ResponseBody ResponseEntity<List<PostDTO>> getUserPosts(@PathVariable String username) {
        boolean canUserSeePrivatePosts = KeycloakRoles.hasRole(KeycloakRoles.ADMIN) || KeycloakRoles.hasRole(KeycloakRoles.PRIVILEGED);
        if (canUserSeePrivatePosts) {
            return ResponseEntity.status(HttpStatus.OK).body(postService.getDTOList(postService.getUserPosts(username)));
        }
        return ResponseEntity.status(HttpStatus.OK).body(postService.getDTOList(postService.getUserPublicPosts(username)));
    }

    @Operation(summary = "Create a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Media was not found with this id",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Description or alt text invalid",
                    content = @Content) })
    @PostMapping(value = "", produces = "application/json")
    public @ResponseBody ResponseEntity<Integer> sendPost(@RequestBody SendPostCommand sendPostCommand,
                                                          @AuthenticationPrincipal Jwt jwt) throws InvalidDescriptionException, InvalidAltException, FileNotFoundException {

        MediaFile media = storageService.getMediaFile(sendPostCommand.getMediaId());
        String alt = sendPostCommand.getAlt();
        String description = sendPostCommand.getDescription();
        boolean visibility = sendPostCommand.isPrivate();
        if (postService.isDescriptionInvalid(description)) {
            throw new InvalidDescriptionException("Too Long Text - must be less than 500 characters");
        }
        if (postService.isAltInvalid(alt)) {
            throw new InvalidAltException("Too long text - must be less than 200 characters");
        }
        String creatorId = jwt.getSubject();
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(media, alt, description, visibility, creatorId));
    }

    @Operation(summary = "Update a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "New post values invalid",
                    content = @Content) })
    @PutMapping(value = "/{id}", produces = "application/json")
    public @ResponseBody ResponseEntity<PostDTO> editPost(@PathVariable(value = "id") int postId, @RequestBody EditPostCommand editPostCommand) throws InvalidDescriptionException, PostNotFoundException, InvalidAltException {
        String alt = editPostCommand.getAlt();
        String description = editPostCommand.getDescription();
        Boolean visibility = editPostCommand.isPrivate();
        if (postService.isDescriptionInvalid(description)) {
            throw new InvalidDescriptionException("Too long text - must be less than 500 characters");
        }
        if (postService.isAltInvalid(alt)) {
            throw new InvalidAltException("Too long text - must be less than 200 characters");
        }
        return ResponseEntity.status(HttpStatus.OK).body(postService.getDTO(postService.updatePost(postId, alt, description, visibility)));
    }

    @Operation(summary = "Delete a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post deleted",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Post or media was not found with this id",
                    content = @Content)})
    @DeleteMapping(value = "/{id}", produces = "application/json")
    public @ResponseBody ResponseEntity<Void> deletePost(@PathVariable(value = "id") int postId) throws PostNotFoundException, FileNotFoundException {
        postService.deletePost(postId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Like a post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post liked",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Post not found",
                    content = @Content)})
    @PutMapping(value = "/like/{id}", produces = "application/json")
    public @ResponseBody ResponseEntity<PostDTO> likePost(@PathVariable(value = "id") int postId, @AuthenticationPrincipal Jwt jwt) throws PostNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getDTO(postService.like(postId, jwt.getSubject())));
    }

}
