package techquack.com.onestar3gram.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import techquack.com.onestar3gram.entities.MediaFile;
import techquack.com.onestar3gram.exceptions.FileNotFoundException;
import techquack.com.onestar3gram.exceptions.StorageException;
import techquack.com.onestar3gram.services.storage.StorageService;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping(value = "/image")
public class ImageController {

    private final StorageService storageService;

    @Autowired
    public ImageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(value = "/")
    public List<MediaFile> getImages() { return  storageService.getAllImages(); }

    @GetMapping(value = "/{id}")
    public MediaFile getImageById(@PathVariable int id) throws FileNotFoundException {
        return storageService.getMediaFile(id);
    }

    @GetMapping(value = "/download/{id}")
    public File downloadImage(@PathVariable int id) {
        return storageService.getFile(id);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MediaFile uploadImage(@RequestParam("file") MultipartFile file) throws StorageException {
        if(!storageService.isValidImage(file)) {
            throw new StorageException("Invalid image");
        }
        return storageService.storeImage(file);
    }
}
