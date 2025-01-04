package techquack.com.onestar3gram.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MediaFileDTO {

    private Integer id;

    private String originName;

    private String generatedName;

    private boolean isVideo;
}
