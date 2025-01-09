package techquack.com.onestar3gram.DTO;

import lombok.Getter;
import lombok.Setter;

public class SendPostCommand {

    @Setter
    @Getter
    private int mediaId;

    @Setter
    @Getter
    private String alt;

    @Setter
    @Getter
    private String description;
    private boolean _private;

    public boolean isPrivate() { return _private; }
    public void setPrivate(boolean aPrivate) { _private = aPrivate; }

}
