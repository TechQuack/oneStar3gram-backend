package techquack.com.onestar3gram.DTO;

import lombok.Getter;
import lombok.Setter;
import techquack.com.onestar3gram.entities.Comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CommentDTO {

    private Integer id;
    private String author;
    private String value;
    private Date postDate;
    private List<String> likers = new ArrayList<>();
}
