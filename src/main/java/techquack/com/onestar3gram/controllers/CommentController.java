package techquack.com.onestar3gram.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import techquack.com.onestar3gram.DTO.CommentDTO;
import techquack.com.onestar3gram.config.KeycloakRoles;
import techquack.com.onestar3gram.entities.Comment;
import techquack.com.onestar3gram.entities.Post;
import techquack.com.onestar3gram.exceptions.comment.CommentInvalidException;
import techquack.com.onestar3gram.exceptions.comment.CommentNotFoundException;
import techquack.com.onestar3gram.exceptions.post.PostInvalidException;
import techquack.com.onestar3gram.exceptions.post.PostNotFoundException;
import techquack.com.onestar3gram.repositories.PostRepository;
import techquack.com.onestar3gram.services.CommentService;

import java.util.List;
import java.util.Objects;

@RestController
public class CommentController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentService commentService;

    @Operation(summary = "Get a comment by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the comment",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Comment not found",
                    content = @Content) })
    @GetMapping(value="comment/{commentId}", produces = "application/json")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable int commentId) throws CommentNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(
                commentService.getDTO(commentService.getCommentById(commentId)));
    }

    @Operation(summary = "Get comments by post id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the comments with the post id",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid post id",
                    content = @Content) })
    @GetMapping(value="post/{postId}/comments", produces = "application/json")
    public ResponseEntity<List<CommentDTO>> getPostComments(@PathVariable int postId) throws PostInvalidException {
        Post p = this.postRepository.findOneById(postId);
        List<Comment> comments = this.commentService.getPostComments(p);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getListDTO(comments));
    }

    @Operation(summary = "Create a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class)) }),
            @ApiResponse(responseCode = "400", description = "Comment value or post id invalid",
                    content = @Content) })
    @PostMapping(value = "post/{postId}/comments/value", produces = "application/json")
    public ResponseEntity<CommentDTO> postComment(@PathVariable int postId, @RequestBody String value,
                                               @AuthenticationPrincipal Jwt jwt) throws CommentInvalidException, PostInvalidException {
        String userId = jwt.getSubject();
        Comment c = this.commentService.createComment(postRepository.findOneById(postId), value, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.getDTO(c));
    }

    @Operation(summary = "Update a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Comment not found",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "New comment value invalid",
                    content = @Content) })
    @PutMapping(value = "comment/{commentId}/value/{value}", produces = "application/json")
    public ResponseEntity<CommentDTO> putComment(@PathVariable int commentId, @PathVariable String value) throws CommentNotFoundException, CommentInvalidException {
        Comment c = this.commentService.getCommentById(commentId);
        c = this.commentService.updateComment(c, value);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getDTO(c));
    }

    @Operation(summary = "Like a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment liked",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Comment not found",
                    content = @Content)})
    @PutMapping(value = "comment/{commentId}/like", produces = "application/json")
    public ResponseEntity<CommentDTO> putLike(@PathVariable int commentId,
                                           @AuthenticationPrincipal Jwt jwt) throws CommentNotFoundException {
        Comment c = this.commentService.getCommentById(commentId);
        c = this.commentService.likeComment(c, jwt.getSubject());
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getDTO(c));
    }

    @Operation(summary = "Delete a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Comment or post not found",
                    content = @Content)})
    @DeleteMapping(value = "comment/{commentId}", produces = "application/json")
    public ResponseEntity<Void> deleteComment(@PathVariable int commentId, @AuthenticationPrincipal Jwt jwt) throws CommentNotFoundException, PostNotFoundException {
        Comment c = this.commentService.getCommentById(commentId);
        if (!Objects.equals(c.getAuthorId(), jwt.getSubject()) && !KeycloakRoles.hasRole(KeycloakRoles.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        this.commentService.deleteComment(c);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
