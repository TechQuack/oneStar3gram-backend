package techquack.com.onestar3gram.controllers;

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

    @GetMapping(value="comment/{commentId}", produces = "application/json")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable int commentId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                commentService.getDTO(commentService.getCommentById(commentId)));
    }

    @GetMapping(value="post/{postId}/comments", produces = "application/json")
    public ResponseEntity<List<CommentDTO>> getPostComments(@PathVariable int postId) {
        Post p = this.postRepository.findOneById(postId);
        List<Comment> comments = this.commentService.getPostComments(p);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getListDTO(comments));
    }

    @PostMapping(value = "post/{postId}/comments/value", produces = "application/json")
    public ResponseEntity<CommentDTO> postComment(@PathVariable int postId, @RequestBody String value,
                                               @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        Comment c = this.commentService.createComment(postRepository.findOneById(postId), value, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.getDTO(c));
    }

    @PutMapping(value = "comment/{commentId}/value/{value}", produces = "application/json")
    public ResponseEntity<CommentDTO> putComment(@PathVariable int commentId, @PathVariable String value) {
        Comment c = this.commentService.getCommentById(commentId);
        c = this.commentService.updateComment(c, value);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getDTO(c));
    }

    @PutMapping(value = "comment/{commentId}/like", produces = "application/json")
    public ResponseEntity<CommentDTO> putLike(@PathVariable int commentId,
                                           @AuthenticationPrincipal Jwt jwt) {
        Comment c = this.commentService.getCommentById(commentId);
        c = this.commentService.likeComment(c, jwt.getSubject());
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getDTO(c));
    }

    @DeleteMapping(value = "comment/{commentId}", produces = "application/json")
    public ResponseEntity<String> deleteComment(@PathVariable int commentId, @AuthenticationPrincipal Jwt jwt) {
        Comment c = this.commentService.getCommentById(commentId);
        if (!Objects.equals(c.getAuthorId(), jwt.getSubject()) && !KeycloakRoles.hasRole(KeycloakRoles.ADMIN)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(jwt.getClaims().toString());
        }
        this.commentService.deleteComment(c);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
