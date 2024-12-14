package techquack.com.onestar3gram.services.storage;

import org.springframework.web.multipart.MultipartFile;
import techquack.com.onestar3gram.entities.MediaFile;
import techquack.com.onestar3gram.exceptions.FileNotFoundException;
import techquack.com.onestar3gram.exceptions.StorageException;

import java.io.File;
import java.util.UUID;

public interface StorageService {

    boolean isValidImage(MultipartFile file);

    boolean isValidVideo(MultipartFile file);

    String getFileOriginalName(int id) throws FileNotFoundException;

    MediaFile getFile(int id);

    MediaFile storeImage(MultipartFile file) throws StorageException;
    MediaFile storeVideo(MultipartFile file) throws StorageException;
}
