package techquack.com.onestar3gram.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import techquack.com.onestar3gram.entities.MediaFile;
import techquack.com.onestar3gram.exceptions.media.FileNotFoundException;
import techquack.com.onestar3gram.exceptions.media.StorageException;
import techquack.com.onestar3gram.services.storage.FileSystemStorageService;
import techquack.com.onestar3gram.services.storage.StorageService;

import java.io.*;
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

    @Operation(summary = "Get videos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Videos found",
                    content = { @Content(mediaType = "application/json")})})
    @GetMapping(value = "")
    public ResponseEntity<List<MediaFile>> getVideos() {
        return  ResponseEntity.status(HttpStatus.OK).body(storageService.getAllVideos()); }

    @Operation(summary = "Get video by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Video found",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Video was not found with this id",
                    content = @Content)})
    @GetMapping(value = "/{id}")
    public ResponseEntity<MediaFile> getVideoById(@PathVariable int id) throws FileNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(storageService.getMediaFile(id));
    }

    @Operation(summary = "Download a video")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Video downloaded",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Video was not found with this id",
                    content = @Content)})
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

    @Operation(summary = "Upload a video")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Image deleted",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Error in storage",
                    content = @Content)})
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaFile> uploadVideo(@RequestParam("file") MultipartFile file) throws StorageException, IOException {
        if (!storageService.isValidVideo(file)) {
            throw new StorageException("Invalid video");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(storageService.storeVideo(file));
    }

    @Operation(summary = "Delete a video")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Video deleted",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Video was not found with this id",
                    content = @Content)})
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable int id) throws FileNotFoundException {
        storageService.deleteFile(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
