package techquack.com.onestar3gram.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public Post getPost(Integer postId) throws PostNotFoundException {
        return postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("post not found - invalid id " + postId));
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPublicPosts() {
        return postRepository.findAll().stream().filter((p) -> !p.isPrivate()).toList();
    }

    public List<Post> getPrivatePosts() {
        return postRepository.findAll().stream().filter(Post::isPrivate).toList();
    }

    public Integer createPost(MediaFile media, String alt, String description, boolean visibility, String creatorId) {
        Post post = new Post();
        post.setMedia(media);
        post.setAlt(alt);
        post.setDescription(description);
        post.setPrivate(visibility);
        post.setCreatorId(creatorId);
        post.setPostDate(new Date());
        postRepository.save(post);
        return post.getId();
    }

    public Post updatePost(Integer postId, String alt, String description, boolean visibility) throws PostNotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("post not found - invalid id " + postId));
        post.setAlt(alt);
        post.setDescription(description);
        post.setPrivate(visibility);
        postRepository.save(post);
        return post;
    }

    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }

    public boolean isPostVisible(Integer postId) throws PostNotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("post not found - invalid id " + postId));
        return post.isPrivate();
    }

    public boolean isDescriptionInvalid(String description) {
        return description != null && description.length() > 500;
    }

    public Post like(Integer postId, String userId) throws PostNotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("post not found - invalid id " + postId));
        if (post.getLikers().contains(userId)) {
            post.getLikers().remove(userId);
        } else {
            post.getLikers().add(userId);
        }
        postRepository.save(post);
        return post;
    }
}
