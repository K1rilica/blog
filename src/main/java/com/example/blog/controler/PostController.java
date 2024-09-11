package com.example.blog.controler;


import com.example.blog.service.FileStorageService;
import com.example.blog.entity.Post;
import com.example.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FileStorageService fileStorageService;

    private static final int PAGE_SIZE = 10;

    @GetMapping("/")
    public String getAllPosts(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Post> postPage = postRepository.findAll(pageable);
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        return "index";
    }

    @GetMapping("/search")
    public String searchPosts(@RequestParam("query") String query, @RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<Post> postPage = postRepository.findByTitleContainingIgnoreCase(query, pageable);
        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("query", query);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", postPage.getTotalPages());
        return "index";
    }

    @GetMapping("/create")
    public String createPostForm(Model model) {
        model.addAttribute("post", new Post());
        return "create";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute Post post, @RequestParam("image") MultipartFile image, @RequestParam("attachment") MultipartFile attachment) throws IOException {
        if (!image.isEmpty()) {
            String imagePath = fileStorageService.storeFile(image);
            post.setImagePath(imagePath);
        }
        if (!attachment.isEmpty()) {
            String attachmentPath = fileStorageService.storeFile(attachment);
            post.setAttachmentPath(attachmentPath);
        }
        postRepository.save(post);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editPostForm(@PathVariable Long id, Model model) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            model.addAttribute("post", optionalPost.get());
            return "edit";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, @ModelAttribute Post postDetails) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setTitle(postDetails.getTitle());
            post.setContent(postDetails.getContent());
            postRepository.save(post);
        }
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postRepository.deleteById(id);
        return "redirect:/";
    }


}
