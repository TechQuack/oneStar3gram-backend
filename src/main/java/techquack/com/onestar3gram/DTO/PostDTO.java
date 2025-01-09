package techquack.com.onestar3gram.DTO;

import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import techquack.com.onestar3gram.entities.MediaFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDTO {
    @Setter
    @Getter
    private Integer id;
    @Setter
    @Getter
    private String creator;
    @Setter
    @Getter
    private Date postDate;
    @Setter
    @Getter
    private String description;

    private boolean _private;

    @Setter
    @Getter
    private List<String> likers = new ArrayList<>();

    @Setter
    @Getter
    @OneToMany
    private List<CommentDTO> comments;

    @Setter
    @Getter
    @OneToOne
    private MediaFile media;

    @Setter
    @Getter
    private String alt;

    public boolean isPrivate() { return _private; }
    public void setPrivate(boolean aPrivate) { _private = aPrivate; }

}
