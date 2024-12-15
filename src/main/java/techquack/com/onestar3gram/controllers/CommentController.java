package techquack.com.onestar3gram.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techquack.com.onestar3gram.entities.Comment;
import techquack.com.onestar3gram.entities.Post;
import techquack.com.onestar3gram.exceptions.comment.CommentInvalidException;
import techquack.com.onestar3gram.exceptions.utils.EmptyException;
import techquack.com.onestar3gram.exceptions.post.PostInvalidException;
import techquack.com.onestar3gram.repositories.PostRepository;
import techquack.com.onestar3gram.services.CommentService;
import java.util.List;

@RestController
public class CommentController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentService commentService;

    @GetMapping(value="comments/{commentId}", produces = "application/json")
    public ResponseEntity<Comment> getCommentById(@PathVariable int commentId) {
        Comment c = this.commentService.getCommentById(commentId);
        return ResponseEntity.status(c == null ? HttpStatus.NOT_FOUND : HttpStatus.OK).body(c);
    }

    @GetMapping(value="posts/{postId}/comments", produces = "application/json")
    public ResponseEntity<List<Comment>> getPostComments(@PathVariable int postId) {
        Post p = this.postRepository.findOneById(postId);
        List<Comment> comments = null;
        try {
            comments = this.commentService.getPostComments(p);
        } catch (PostInvalidException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(comments == null ? HttpStatus.NOT_FOUND : HttpStatus.OK).body(comments);
    }

    @GetMapping(value = "comments/{commentId}/post", produces = "application/json")
    public ResponseEntity<Post> getPostFromComment(@PathVariable int commentId) {
        Comment c = this.commentService.getCommentById(commentId);
        Post p = null;
        try {
            p = this.commentService.getPostFromComment(c);
        } catch (CommentInvalidException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(p == null ? HttpStatus.NOT_FOUND : HttpStatus.OK).body(p);
    }

    @PostMapping(value = "posts/{postId}/comments/value/{value}", produces = "application/json")
    public ResponseEntity<Comment> postComment(@PathVariable int postId, @PathVariable String value) {
        Comment c = null;
        try {
            c = this.commentService.createComment(postRepository.findOneById(postId), value);
        } catch (EmptyException | PostInvalidException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(c == null ? HttpStatus.CONFLICT : HttpStatus.CREATED).body(c);
    }

    @PutMapping(value = "comments/{commentId}/value/{value}", produces = "application/json")
    public ResponseEntity<Comment> putComment(@PathVariable int commentId, @PathVariable String value) {
        Comment c = this.commentService.getCommentById(commentId);
        try {
            c = this.commentService.updateComment(c, value);
        } catch (CommentInvalidException | EmptyException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(c == null ? HttpStatus.NOT_FOUND : HttpStatus.OK).body(c);
    }

    @PutMapping(value = "comments/{commentId}/like", produces = "application/json")
    public ResponseEntity<Comment> putLike(@PathVariable int commentId) {
        Comment c = this.commentService.getCommentById(commentId);
        try {
            this.commentService.likeComment(c);
        } catch (CommentInvalidException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(c == null ? HttpStatus.NOT_FOUND : HttpStatus.OK).body(c);
    }

    @DeleteMapping(value = "comments/{commentId}", produces = "application/json")
    public ResponseEntity<Comment> deleteComment(@PathVariable int commentId) {
        Comment c = this.commentService.getCommentById(commentId);
        try {
            this.commentService.deleteComment(c);
        } catch (CommentInvalidException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(c == null ? HttpStatus.NOT_FOUND : HttpStatus.OK).body(c);
    }
}
