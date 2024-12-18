package techquack.com.onestar3gram.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private AppUser creator;

    private Date postDate;
    private String description;
    private boolean isPrivate;
    private int likes;
    @OneToMany
    private List<Comment> comments;

    @OneToOne
    private MediaFile media;

    private String alt;

    public int getId() {
        return id;
    }

    public AppUser getCreator() {
        return creator;
    }

    public void setCreator(AppUser creator) {
        this.creator = creator;
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

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
