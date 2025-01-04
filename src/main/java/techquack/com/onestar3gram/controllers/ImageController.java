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
import techquack.com.onestar3gram.services.storage.StorageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping(value = "/image")
public class ImageController {

    private final StorageService storageService;

    @Autowired
    public ImageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @Operation(summary = "Get images")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Images found",
                    content = { @Content(mediaType = "application/json")})})
    @GetMapping(value = "")
    public ResponseEntity<List<MediaFile>> getImages() { return  ResponseEntity.status(HttpStatus.OK).body(storageService.getAllImages()); }

    @Operation(summary = "Get image by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image found",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Image was not found with this id",
                    content = @Content)})
    @GetMapping(value = "/{id}")
    public ResponseEntity<MediaFile> getImageById(@PathVariable int id) throws FileNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(storageService.getMediaFile(id));
    }

    @Operation(summary = "Download an image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image downloaded",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Image was not found with this id",
                    content = @Content)})
    @GetMapping(value = "/download/{id}")
    public ResponseEntity<StreamingResponseBody> downloadImage(@PathVariable int id) throws FileNotFoundException {
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

    @Operation(summary = "Upload an image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Image uploaded",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Error in storage",
                    content = @Content)})
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaFile> uploadImage(@RequestParam("file") MultipartFile file) throws StorageException, IOException {
        if(!storageService.isValidImage(file)) {
            throw new StorageException("Invalid image");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(storageService.storeImage(file));
    }

    @Operation(summary = "Delete an image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image deleted",
                    content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Image was not found with this id",
                    content = @Content)})
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable int id) throws FileNotFoundException {
        storageService.deleteFile(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
