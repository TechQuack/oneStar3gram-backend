package techquack.com.onestar3gram.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import techquack.com.onestar3gram.entities.MediaFile;
import techquack.com.onestar3gram.exceptions.FileNotFoundException;
import techquack.com.onestar3gram.exceptions.StorageException;
import techquack.com.onestar3gram.services.storage.FileSystemStorageService;
import techquack.com.onestar3gram.services.storage.StorageService;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping(value = "/video")
public class VideoController {

    private StorageService storageService;

    @Autowired
    public VideoController(FileSystemStorageService fileSystemStorageService) {
        this.storageService = fileSystemStorageService;
    }
    @GetMapping(value = "/")
    public List<MediaFile> getVideos() {
        return storageService.getAllVideos();
    }

    @GetMapping(value = "/{id}")
    public MediaFile getVideoById(@PathVariable int id) throws FileNotFoundException {
        return storageService.getMediaFile(id);
    }

    @GetMapping(value = "/download/{id}")
    public File downloadVideo(@PathVariable int id) {
        return storageService.getFile(id);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MediaFile uploadVideo(@RequestParam("file") MultipartFile file) throws StorageException {
        if (!storageService.isValidVideo(file)) {
            throw new StorageException("Invalid video");
        }
        return storageService.storeVideo(file);
    }
}
