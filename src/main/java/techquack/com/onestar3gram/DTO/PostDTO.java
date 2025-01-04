package techquack.com.onestar3gram.DTO;

import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import techquack.com.onestar3gram.entities.MediaFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDTO {

    private Integer id;

    private String creator;

    private Date postDate;
    private String description;
    private boolean isPrivate;

    private List<String> likers = new ArrayList<>();
    @OneToMany
    private List<CommentDTO> comments;

    @OneToOne
    private MediaFile media;

    private String alt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) { this.id = id; }

    public String getCreator() {
        return creator;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public void setCreator(String creator) {
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

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public List<CommentDTO> getComments() {
        return comments;
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
    public void setLikers(List<String> likers) { this.likers = likers; }

}
