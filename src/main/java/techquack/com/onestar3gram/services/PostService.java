package techquack.com.onestar3gram.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techquack.com.onestar3gram.DTO.CommentDTO;
import techquack.com.onestar3gram.DTO.PostDTO;
import techquack.com.onestar3gram.entities.Comment;
import techquack.com.onestar3gram.entities.MediaFile;
import techquack.com.onestar3gram.entities.Post;
import techquack.com.onestar3gram.exceptions.FileNotFoundException;
import techquack.com.onestar3gram.exceptions.post.PostNotFoundException;
import techquack.com.onestar3gram.repositories.PostRepository;
import techquack.com.onestar3gram.services.storage.StorageService;

import java.util.Date;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final int MAX_DESCRIPTION_LENGTH = 500;
    private final int MAX_ALT_LENGTH = 200;

    private final AdminClientService adminClientService;

    private final CommentService commentService;

    private final StorageService storageService;

    @Autowired
    public PostService(PostRepository postRepository, AdminClientService adminClientService,
                       CommentService commentService, StorageService storageService) {
        this.adminClientService = adminClientService;
        this.postRepository = postRepository;
        this.commentService = commentService;
        this.storageService = storageService;
    }

    public Post getPost(int postId) throws PostNotFoundException {
        return postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("post not found - invalid id " + postId));
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPublicPosts() {
        return postRepository.findAll().stream().filter((p) -> !p.isPrivate()).toList();
    }

    public List<Post> getUserPosts(String username) {
        return postRepository.findAll().stream().filter((p) -> {
            String author = adminClientService.searchByKeycloakId(p.getCreatorId()).get(0).getUsername();
            return username.equals(author);
        }).toList();
    }

    public List<Post> getUserPublicPosts(String username) {
        return postRepository.findAll().stream().filter((p) -> {
            String author = adminClientService.searchByKeycloakId(p.getCreatorId()).get(0).getUsername();
            return !p.isPrivate() && username.equals(author);
        }).toList();
    }

    public int createPost(MediaFile media, String alt, String description, boolean visibility, String creatorId) {
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

    public Post updatePost(int postId, String alt, String description, Boolean visibility) throws PostNotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("post not found - invalid id " + postId));
        if (alt != null) {
            post.setAlt(alt);
        }
        if (description != null) {
            post.setDescription(description);
        }
        if (visibility != null) {
            post.setPrivate(visibility);
        }
        postRepository.save(post);
        return post;
    }

    public void deletePost(int postId) throws PostNotFoundException, FileNotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("post not found - invalid id " + postId));
        int id = post.getMedia().getId();
        post.setMedia(null);
        postRepository.save(post);
        storageService.deleteFile(id);
        postRepository.deleteById(postId);
    }

    public boolean isDescriptionInvalid(String description) {
        return description != null && description.length() > MAX_DESCRIPTION_LENGTH;
    }

    public boolean isAltInvalid(String alt) {
        return alt != null && alt.length() > MAX_ALT_LENGTH;
    }

    public Post like(int postId, String userId) throws PostNotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("post not found - invalid id " + postId));
        String username = adminClientService.searchByKeycloakId(userId).get(0).getUsername();
        if (post.getLikers().contains(username)) {
            post.getLikers().remove(username);
        } else {
            post.getLikers().add(username);
        }
        postRepository.save(post);
        return post;
    }

    public PostDTO getDTO(Post post) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setAlt(post.getAlt());
        dto.setDescription(post.getDescription());
        dto.setIsPrivate(post.isPrivate());
        dto.setPostDate(post.getPostDate());
        dto.setMedia(post.getMedia());
        dto.setComments(getCommentsDTO(post.getComments()));
        dto.setLikers(post.getLikers());
        dto.setCreator(adminClientService.searchByKeycloakId(post.getCreatorId()).get(0).getUsername());
        return dto;
    }

    public List<PostDTO> getDTOList(List<Post> posts) {
        return posts.stream()
                .map(this::getDTO)
                .toList();
    }

    private List<CommentDTO> getCommentsDTO(List<Comment> comments) {
        return commentService.getListDTO(comments);
    }
}
