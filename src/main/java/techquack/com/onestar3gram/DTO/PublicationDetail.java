package techquack.com.onestar3gram.DTO;

import techquack.com.onestar3gram.entities.AppUser;
import techquack.com.onestar3gram.entities.MediaFile;

public class PublicationDetail {

    private MediaFile media;
    private AppUser creator;

    public MediaFile getMedia() {
        return media;
    }

    public AppUser getCreator() {
        return creator;
    }

    public void setMedia(MediaFile media) {
        this.media = media;
    }

    public void setCreator(AppUser creator) {
        this.creator = creator;
    }
}
