package techquack.com.onestar3gram.services.storage;

import org.springframework.web.multipart.MultipartFile;
import techquack.com.onestar3gram.entities.MediaFile;
import techquack.com.onestar3gram.exceptions.media.FileNotFoundException;
import techquack.com.onestar3gram.exceptions.media.StorageException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface StorageService {

    boolean isValidImage(MultipartFile file) throws  IOException;

    boolean isValidVideo(MultipartFile file) throws IOException;

    MediaFile getMediaFile(int id) throws FileNotFoundException;

    File getFile(int id);

    List<MediaFile> getAllVideos();

    List<MediaFile> getAllImages();

    MediaFile storeImage(MultipartFile file) throws StorageException;
    MediaFile storeVideo(MultipartFile file) throws StorageException;

    void deleteFile(int id) throws FileNotFoundException;
}
