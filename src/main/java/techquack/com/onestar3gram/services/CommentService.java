package techquack.com.onestar3gram.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techquack.com.onestar3gram.DTO.CommentDTO;
import techquack.com.onestar3gram.entities.Comment;
import techquack.com.onestar3gram.entities.Post;
import techquack.com.onestar3gram.exceptions.comment.CommentInvalidException;
import techquack.com.onestar3gram.exceptions.utils.EmptyException;
import techquack.com.onestar3gram.exceptions.post.PostInvalidException;
import techquack.com.onestar3gram.repositories.CommentRepository;
import techquack.com.onestar3gram.repositories.PostRepository;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    AdminClientService adminClientService;

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

    public Comment createComment(Post post, String value, String userId) throws EmptyException, PostInvalidException {

        if (post == null) {
            throw new PostInvalidException();
        }

        if (value == null || value.isEmpty()) {
            throw new EmptyException();
        }

        Comment c = new Comment();
        c.setPostDate(new Date());
        c.setValue(value);
        c.setAuthorId(userId);
        this.commentRepository.save(c);

        post.addComment(c);
        this.postRepository.save(post);

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

        Post p = this.postRepository.findByCommentId(comment.getId());

        if (p == null) {
            throw new PostInvalidException();
        }

        p.removeComment(comment);
        this.commentRepository.delete(comment);
        this.postRepository.save(p);
        return comment;
    }

    public Comment likeComment(Comment comment, String userId) throws CommentInvalidException {
        if (comment == null) {
            throw new CommentInvalidException();
        }
        if (comment.getLikers().contains(userId)) {
            comment.removeLike(userId);
        } else {
            comment.addLike(userId);
        }
        this.commentRepository.save(comment);
        return comment;
    }

    public List<CommentDTO> getListDTO(List<Comment> comments) {
        return comments.stream()
                .map(this::getDTO)
                .toList();
    }

    public CommentDTO getDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setAuthor(adminClientService.searchByKeycloakId(comment.getAuthorId()).get(0).getUsername());
        dto.setId(comment.getId());
        dto.setValue(comment.getValue());
        dto.setPostDate(comment.getPostDate());
        dto.setLikers(getUsers(comment.getLikers()));
        return dto;
    }

    private List<String> getUsers(List<String> likers) {
       return likers.stream()
                    .map(id -> adminClientService.searchByKeycloakId(id).get(0).getUsername())
                    .toList();
    }
}
