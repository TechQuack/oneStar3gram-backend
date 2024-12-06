package techquack.com.onestar3gram.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import techquack.com.onestar3gram.entities.MediaFile;

import java.util.List;

public interface MediaFileRepository extends JpaRepository<MediaFile, Integer> {

    List<MediaFile> findBy();
}
