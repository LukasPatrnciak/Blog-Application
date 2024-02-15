package sk.lukaspatrnciak.blogapplication.controllers;

import sk.lukaspatrnciak.blogapplication.services.FileService;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {
    private final FileService fileService;

    public ImageController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/images/{id}")
    public Resource getImage(@PathVariable("id") String imageUri) {
        return fileService.load(imageUri);
    }


}
