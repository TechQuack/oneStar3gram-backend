package techquack.com.onestar3gram.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String creatorId;

    private Date postDate;
    private String description;
    private boolean isPrivate;

    @ElementCollection
    private List<String> likers = new ArrayList<>();
    @OneToMany
    private List<Comment> comments;

    @OneToOne
    private MediaFile media;

    private String alt;

    public Integer getId() {
        return id;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void removeComment(Comment comment) {
        this.comments.remove(comment);
    }

    public MediaFile getMedia() {
        return media;
    }

    public void setMedia(MediaFile media) {
        this.media = media;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
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
