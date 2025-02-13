package techquack.com.onestar3gram.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String authorId;
    private String value;
    private Date postDate;

    @ElementCollection
    private List<String> likers = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public String getAuthorId() { return authorId; }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
        }
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
