package techquack.com.onestar3gram.DTO;

import techquack.com.onestar3gram.entities.Comment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentDTO {

    private Integer id;
    private String author;
    private String value;
    private Date postDate;
    private List<String> likers = new ArrayList<>();
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) { this.id = id; }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() { return author; }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public int getLikeCount() {
        return likers.size();
    }

    public void setLikers(List<String> likers) { this.likers = likers; }

    public List<String> getLikers() {
        return likers;
    }

    public void addLike(String keycloakId) {
        this.likers.add(keycloakId);
    }

    public void removeLike(String keycloakId) {
        this.likers.remove(keycloakId);
    }
}
