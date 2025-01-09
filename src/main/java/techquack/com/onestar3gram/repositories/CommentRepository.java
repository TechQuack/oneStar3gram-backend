package techquack.com.onestar3gram.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import techquack.com.onestar3gram.entities.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findBy();

    Comment findOneById(int commentId);
}
