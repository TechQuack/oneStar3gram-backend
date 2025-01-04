package techquack.com.onestar3gram.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techquack.com.onestar3gram.DTO.CommentDTO;
import techquack.com.onestar3gram.entities.Comment;
import techquack.com.onestar3gram.entities.Post;
import techquack.com.onestar3gram.exceptions.comment.CommentInvalidException;
import techquack.com.onestar3gram.exceptions.comment.CommentNotFoundException;
import techquack.com.onestar3gram.exceptions.post.PostNotFoundException;
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

    public Comment getCommentById(int id) throws CommentNotFoundException {
        Comment c = this.commentRepository.findOneById(id);
        if (c == null) {
            throw new CommentNotFoundException("Comment with id " + id + " does not exist");
        }
        return c;
    }

    public List<Comment> getPostComments(Post post) throws PostInvalidException {
        if (post == null) {
            throw new PostInvalidException("A post must exist to get its comments");
        }
        return post.getComments();
    }

    public Comment createComment(Post post, String value, String userId) throws PostInvalidException, CommentInvalidException {

        if (post == null) {
            throw new PostInvalidException("A post must exist to create a comment");
        }

        if (value == null || value.isEmpty()) {
            throw new CommentInvalidException("There must be a value to the comment");
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

    public Comment updateComment(Comment comment, String value) throws CommentInvalidException {
        if (comment == null) {
            throw new CommentInvalidException("There must be a comment to update its value");
        }
        if (value == null || value.isEmpty()) {
            throw new CommentInvalidException("There must be a value to update the comment");
        }
        comment.setValue(value);
        this.commentRepository.save(comment);
        return comment;
    }

    public void deleteComment(Comment comment) throws PostNotFoundException, CommentNotFoundException {
        if (comment == null) {
            throw new CommentNotFoundException("There must be a comment to delete it");
        }

        Post p = this.postRepository.findOneByCommentsIsContaining(comment);

        if (p == null) {
            throw new PostNotFoundException("There must be a post associated to the comment");
        }

        p.removeComment(comment);
        this.commentRepository.delete(comment);
        this.postRepository.save(p);
    }

    public Comment likeComment(Comment comment, String userId) throws CommentNotFoundException {
        if (comment == null) {
            throw new CommentNotFoundException("There must be a comment to like it");
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
