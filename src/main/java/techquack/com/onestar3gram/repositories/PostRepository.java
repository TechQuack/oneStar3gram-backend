package techquack.com.onestar3gram.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import techquack.com.onestar3gram.entities.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findBy();

    Post findOneById(int postId);
}
