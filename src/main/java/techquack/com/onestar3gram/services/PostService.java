package techquack.com.onestar3gram.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techquack.com.onestar3gram.entities.AppUser;
import techquack.com.onestar3gram.entities.MediaFile;
import techquack.com.onestar3gram.entities.Post;
import techquack.com.onestar3gram.exceptions.PostNotFoundException;
import techquack.com.onestar3gram.repositories.PostRepository;

import java.util.Date;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post getPost(Integer id) throws PostNotFoundException {
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("post not found - invalid id " + id));
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Integer createPost(MediaFile media, String alt, String description, boolean visibility, AppUser creator) {
        Post post = new Post();
        post.setMedia(media);
        post.setAlt(alt);
        post.setDescription(description);
        post.setPrivate(visibility);
        post.setCreator(creator);
        post.setPostDate(new Date());
        postRepository.save(post);
        return post.getId();
    }

    public Post updatePost(Integer id, String alt, String description, boolean visibility) throws PostNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("post not found - invalid id " + id));
        post.setAlt(alt);
        post.setDescription(description);
        post.setPrivate(visibility);
        postRepository.save(post);
        return post;
    }

    public void deletePost(Integer id) {
        postRepository.deleteById(id);
    }

    public boolean isPostVisible(Integer id) throws PostNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("post not found - invalid id " + id));
        return post.isPrivate();
    }

    public boolean isTitleValid(String title) {
        return title != null && title.length() >= 3 && title.length() <= 100;
    }

    public boolean isDescriptionValid(String description) {
        return description == null || description.length() <= 500;
    }
}
