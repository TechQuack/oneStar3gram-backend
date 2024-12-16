package techquack.com.onestar3gram.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techquack.com.onestar3gram.entities.AppUser;
import techquack.com.onestar3gram.entities.MediaFile;
import techquack.com.onestar3gram.entities.Post;
import techquack.com.onestar3gram.exceptions.InvalidDescriptionException;
import techquack.com.onestar3gram.exceptions.PostNotFoundException;
import techquack.com.onestar3gram.services.PostService;

import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public @ResponseBody Post getPost(@PathVariable Integer id) throws PostNotFoundException {
        return postService.getPost(id);
    }

    @GetMapping(value = "", produces = "application/json")
    public @ResponseBody List<Post> getAllPosts() {
        return postService.getAllPosts();
    }


}
