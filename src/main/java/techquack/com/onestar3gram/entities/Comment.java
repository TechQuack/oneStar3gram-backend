package techquack.com.onestar3gram.entities;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private AppUser AppUser;
    private String value;
    private Date postDate;
    private int likeCount;

    public Integer getId() {
        return id;
    }

    public AppUser getAppUser() {
        return AppUser;
    }

    public void setAppUser(AppUser AppUser) {
        this.AppUser = AppUser;
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
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
