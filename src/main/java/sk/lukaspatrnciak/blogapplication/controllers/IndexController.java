package sk.lukaspatrnciak.blogapplication.controllers;

import sk.lukaspatrnciak.blogapplication.models.Post;
import sk.lukaspatrnciak.blogapplication.services.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class IndexController {

    private final PostService postService;

    public IndexController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Post> posts = postService.getAll();
        model.addAttribute("posts", posts);
        return "index";
    }
}
