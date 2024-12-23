package techquack.com.onestar3gram.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import techquack.com.onestar3gram.entities.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findBy();

    Post findOneById(int postId);

    @Query("SELECT p FROM Post p WHERE :id MEMBER OF p.comments")
    Post findByCommentId(Integer id);
}
