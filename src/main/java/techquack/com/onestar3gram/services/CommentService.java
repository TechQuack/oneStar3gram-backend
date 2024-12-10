package techquack.com.onestar3gram.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techquack.com.onestar3gram.entities.Comment;
import techquack.com.onestar3gram.entities.Post;
import techquack.com.onestar3gram.repositories.CommentRepository;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    public Comment getCommentById(int id) {
        return this.commentRepository.findByOneId(id);
    }

    public List<Comment> getPostComments(Post post) {
        if (post == null) {
            return null;
        }
        return post.getComments();
    }

    public Post getPostFromComment(Comment comment) {
        if (comment == null) {
            return null;
        }

        return comment.getPost();
    }

    public Comment createComment(Post post, String value) {
        if (post == null || value == null || value.isEmpty()) {
            return null;
        }

        Comment c = new Comment();
        c.setPost(post);
        c.setValue(value);

        this.commentRepository.save(c);
        return c;
    }

    public Comment updateComment(Comment comment, String value) {
        if (comment == null || value == null || value.isEmpty()) {
            return comment;
        }
        comment.setValue(value);
        this.commentRepository.save(comment);
        return comment;
    }

    public Comment deleteComment(Comment comment) {
        if (comment == null) {
            return null;
        }
        this.commentRepository.delete(comment);
        return comment;
    }

    public Comment likeComment(Comment comment) {
        if (comment == null) {
            return null;
        }

        comment.setLikeCount(comment.getLikeCount() + 1);
        this.commentRepository.save(comment);
        return comment;
    }
}
