package techquack.com.onestar3gram.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import techquack.com.onestar3gram.entities.MediaFile;
import techquack.com.onestar3gram.exceptions.FileNotFoundException;
import techquack.com.onestar3gram.exceptions.StorageException;
import techquack.com.onestar3gram.services.storage.FileSystemStorageService;
import techquack.com.onestar3gram.services.storage.StorageService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping(value = "/video")
public class VideoController {

    private final StorageService storageService;

    @Autowired
    public VideoController(FileSystemStorageService fileSystemStorageService) {
        this.storageService = fileSystemStorageService;
    }
    @GetMapping(value = "")
    public List<MediaFile> getVideos() {
        return storageService.getAllVideos();
    }

    @GetMapping(value = "/{id}")
    public MediaFile getVideoById(@PathVariable int id) throws FileNotFoundException {
        return storageService.getMediaFile(id);
    }

    @GetMapping(value = "/download/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> downloadVideo(@PathVariable int id) throws FileNotFoundException, IOException {
        File file = storageService.getFile(id);
        MediaFile mediaFile = storageService.getMediaFile(id);
        StreamingResponseBody stream = outputStream -> {
            Files.copy(file.toPath(), outputStream);
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + mediaFile.getOriginName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(stream);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MediaFile uploadVideo(@RequestParam("file") MultipartFile file) throws StorageException, IOException {
        if (!storageService.isValidVideo(file)) {
            throw new StorageException("Invalid video");
        }
        return storageService.storeVideo(file);
    }

    @DeleteMapping(value = "/{id}")
    public void uploadVideo(@PathVariable int id) throws FileNotFoundException {
        storageService.deleteFile(id);
    }
}
