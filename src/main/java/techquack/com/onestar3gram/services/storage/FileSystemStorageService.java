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

import javax.print.attribute.standard.Media;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@EnableConfigurationProperties(StorageProperties.class)
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) throws StorageException {
        if(properties.getLocation().trim().isEmpty()){
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
    }

    public boolean isValidImage(MultipartFile file) {
        throw new NotImplementedException(); //TODO
    }

    public boolean isValidVideo(MultipartFile file) {
        throw new NotImplementedException(); //TODO
    }

    public String getFileOriginalName(int id) throws FileNotFoundException {
        MediaFile mediaFile = mediaFileRepository.findOneBy(id);
        if(mediaFile == null) {
            throw new FileNotFoundException("This file id does not exist");
        }
        return mediaFile.getOriginName();
    }

    public MediaFile getFile(int id) {
        return mediaFileRepository.findOneBy(id);
    }

    public MediaFile storeVideo(MultipartFile video) throws StorageException {
        String newName = generateFileName();
        storeFile(video, newName);
        return createMediaFile(video, true, newName);
    }

    public MediaFile storeImage(MultipartFile image) throws StorageException {
        String newName = generateFileName();
        storeFile(image, newName);
        return createMediaFile(image, false, newName);
    }

   private String generateFileName() {
       return LocalDateTime.now() + "_" + UUID.randomUUID();
   }

    private MediaFile createMediaFile(MultipartFile file, boolean isVideo, String newName) {
        MediaFile newMediaFile = new MediaFile();
        newMediaFile.setGeneratedName(newName);
        newMediaFile.setOriginName(file.getName());
        newMediaFile.setVideo(isVideo);
        mediaFileRepository.save(newMediaFile);
        return newMediaFile;
    }

    private void storeFile(MultipartFile file, String newName) throws StorageException {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
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