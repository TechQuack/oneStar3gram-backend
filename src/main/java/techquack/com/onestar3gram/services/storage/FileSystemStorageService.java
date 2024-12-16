package techquack.com.onestar3gram.services.storage;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import techquack.com.onestar3gram.entities.MediaFile;
import techquack.com.onestar3gram.exceptions.FileNotFoundException;
import techquack.com.onestar3gram.exceptions.StorageException;
import techquack.com.onestar3gram.repositories.MediaFileRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@EnableConfigurationProperties(StorageProperties.class)
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    private final MediaFileRepository mediaFileRepository;

    @Autowired
    public FileSystemStorageService(StorageProperties properties, MediaFileRepository mediaFileRepository) throws StorageException {
        if(properties.getLocation().trim().isEmpty()){
            throw new StorageException("File upload location can not be Empty.");
        }
        this.mediaFileRepository = mediaFileRepository;
        this.rootLocation = Paths.get(properties.getLocation());
    }

    public boolean isValidImage(MultipartFile file) {
        throw new NotImplementedException(); //TODO
    }

    public boolean isValidVideo(MultipartFile file) {
        return true; //TODO
    }

    public MediaFile getMediaFile(int id) throws FileNotFoundException {
        MediaFile mediaFile = mediaFileRepository.findOneById(id);
        if(mediaFile == null) {
            throw new FileNotFoundException("This file id does not exist");
        }
        return mediaFile;
    }

    public File getFile(int id) {
        MediaFile mediaFile = mediaFileRepository.findOneById(id);
        return new File(String.valueOf(
                this.rootLocation.resolve(Paths.get(mediaFile.getGeneratedName())))
        );
    }

    public List<MediaFile> getAllVideos() {
        return mediaFileRepository.findByIsVideo(true);
    }

    public List<MediaFile> getAllImages() {
        return mediaFileRepository.findByIsVideo(false);
    }

    public MediaFile storeVideo(MultipartFile video) throws StorageException {
        String filename = video.getOriginalFilename();
        String newName = generateFileName() + "." + filename.substring(filename.lastIndexOf(".") + 1);
        storeFile(video, newName);
        return createMediaFile(video, true, newName);
    }

    public MediaFile storeImage(MultipartFile image) throws StorageException {
        String filename = image.getOriginalFilename();
        String newName = generateFileName() + "." + filename.substring(filename.lastIndexOf(".") + 1);
        storeFile(image, newName);
        return createMediaFile(image, false, newName);
    }

   private String generateFileName() {
       return LocalDateTime.now() + "_" + UUID.randomUUID();
   }

    private MediaFile createMediaFile(MultipartFile file, boolean isVideo, String newName) {
        MediaFile newMediaFile = new MediaFile();
        newMediaFile.setGeneratedName(newName);
        newMediaFile.setOriginName(file.getOriginalFilename());
        newMediaFile.setVideo(isVideo);
        mediaFileRepository.save(newMediaFile);
        return newMediaFile;
    }

    private void storeFile(MultipartFile file, String newName) throws StorageException {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path uploadDirectory = this.rootLocation.normalize().toAbsolutePath();
            if (!Files.exists(uploadDirectory)) {
                Files.createDirectory(uploadDirectory);
            }
            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(newName))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }
}
