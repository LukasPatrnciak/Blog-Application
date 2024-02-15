package sk.lukaspatrnciak.blogapplication.controllers;

import sk.lukaspatrnciak.blogapplication.models.Account;
import sk.lukaspatrnciak.blogapplication.models.Post;
import sk.lukaspatrnciak.blogapplication.services.AccountService;
import sk.lukaspatrnciak.blogapplication.services.FileService;
import sk.lukaspatrnciak.blogapplication.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Optional;

@Controller
@Slf4j
public class PostController {

    private final PostService postService;
    private final AccountService accountService;
    private final FileService fileService;

    public PostController(PostService postService, AccountService accountService, FileService fileService) {
        this.postService = postService;
        this.accountService = accountService;
        this.fileService = fileService;
    }

    @GetMapping("/posts/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = this.postService.getById(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post";
        } else {
            return "none";
        }
    }

    @PostMapping("/posts/{id}")
    @PreAuthorize("isAuthenticated()")
    public String updatePost(@PathVariable Long id, Post post, @RequestParam("file") MultipartFile file) {

        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();

            existingPost.setTitle(post.getTitle());
            existingPost.setBody(post.getBody());

            try {
                fileService.save(file);
                existingPost.setImageFilePath(file.getOriginalFilename());
            } catch (Exception e) {
                log.error("Error processing file: {}", file.getOriginalFilename());
            }

            postService.save(existingPost);
        }

        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/posts/new")
    @PreAuthorize("isAuthenticated()")
    public String createNewPost(Model model) {

        Post post = new Post();
        model.addAttribute("post", post);
        return "post_new";
    }

    @PostMapping("/posts/new")
    @PreAuthorize("isAuthenticated()")
    public String createNewPost(@ModelAttribute Post post, @RequestParam("file") MultipartFile file, Principal principal) {
        String authUsername = "anonymousUser";
        if (principal != null) {
            authUsername = principal.getName();
        }

        Account account = accountService.findOneByEmail(authUsername).orElseThrow(() -> new IllegalArgumentException("Account not found"));

        try {
            fileService.save(file);
            post.setImageFilePath(file.getOriginalFilename());
        } catch (Exception e) {
            log.error("Error processing file: {}", file.getOriginalFilename());
        }

        post.setAccount(account);
        postService.save(post);
        return "redirect:/";
    }

    @GetMapping("/posts/{id}/edit")
    @PreAuthorize("isAuthenticated()")
    public String getPostForEdit(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = postService.getById(id);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post_edit";
        } else {
            return "none";
        }
    }

    @GetMapping("/posts/{id}/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deletePost(@PathVariable Long id) {
        Optional<Post> optionalPost = postService.getById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            postService.delete(post);

            return "redirect:/";
        } else {
            return "none";
        }
    }

}
