package techquack.com.onestar3gram.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.*;
import org.mockito.junit.jupiter.MockitoExtension;
import techquack.com.onestar3gram.entities.Post;
import techquack.com.onestar3gram.exceptions.post.PostNotFoundException;
import techquack.com.onestar3gram.repositories.PostRepository;
import techquack.com.onestar3gram.services.storage.StorageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PostServiceUnitTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    public void getPost_withValidId_shouldReturnValidPost() {
        //Given
        Post post = new Post();
        Mockito.when(postRepository.findById(1)).thenReturn(Optional.of(post));
        //When
        Post result = null;
        try {
            result = postService.getPost(1);
        } catch (PostNotFoundException e) {
            // Will not access it
        }
        //Then
        assertThat(result).isEqualTo(post);
    }

    @Test
    public void getPost_withInvalidId_shouldThrowPostNotFoundException() {
        //Given
        Mockito.when(postRepository.findById(1)).thenReturn(Optional.empty());
        //When
        assertThatExceptionOfType(PostNotFoundException.class).isThrownBy(
                //Then
                () -> postService.getPost(1)
        ).withMessage("post not found - invalid id 1" );
    }

    @Test
    public void getAllPosts_shouldReturnValidListPosts() {
        //Given
        Post post = new Post();
        List<Post> posts = new ArrayList<>();
        posts.add(post);
        Mockito.when(postRepository.findAll()).thenReturn(posts);
        //When
        List<Post> result = postService.getAllPosts();
        //Then
        assertThat(result).isEqualTo(posts);
    }

}
