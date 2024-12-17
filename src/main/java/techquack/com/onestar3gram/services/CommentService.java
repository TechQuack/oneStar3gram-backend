package techquack.com.onestar3gram.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techquack.com.onestar3gram.entities.Comment;
import techquack.com.onestar3gram.entities.Post;
import techquack.com.onestar3gram.exceptions.comment.CommentInvalidException;
import techquack.com.onestar3gram.exceptions.utils.EmptyException;
import techquack.com.onestar3gram.exceptions.post.PostInvalidException;
import techquack.com.onestar3gram.repositories.CommentRepository;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    public Comment getCommentById(int id) {
        Comment c = this.commentRepository.findOneById(id);
        if (c == null) {
            throw new CommentInvalidException();
        }
        return c;
    }

    public List<Comment> getPostComments(Post post) throws PostInvalidException {
        if (post == null) {
            throw new PostInvalidException();
        }
        return post.getComments();
    }

    public Post getPostFromComment(Comment comment) throws CommentInvalidException {
        if (comment == null) {
            throw new CommentInvalidException();
        }

        return comment.getPost();
    }

    public Comment createComment(Post post, String value) throws EmptyException, PostInvalidException {
        if (post == null) {
            throw new PostInvalidException();
        }

        if (value == null || value.isEmpty()) {
            throw new EmptyException();
        }

        Comment c = new Comment();
        c.setPost(post);
        c.setValue(value);

        this.commentRepository.save(c);
        return c;
    }

    public Comment updateComment(Comment comment, String value) throws CommentInvalidException, EmptyException {
        if (comment == null) {
            throw new CommentInvalidException();
        }
        if (value == null || value.isEmpty()) {
            throw new EmptyException();
        }
        comment.setValue(value);
        this.commentRepository.save(comment);
        return comment;
    }

    public Comment deleteComment(Comment comment) throws CommentInvalidException {
        if (comment == null) {
            throw new CommentInvalidException();
        }

        this.commentRepository.delete(comment);
        return comment;
    }

    public Comment likeComment(Comment comment) throws CommentInvalidException {
        if (comment == null) {
            throw new CommentInvalidException();
        }

        comment.setLikeCount(comment.getLikeCount() + 1);
        this.commentRepository.save(comment);
        return comment;
    }
}
