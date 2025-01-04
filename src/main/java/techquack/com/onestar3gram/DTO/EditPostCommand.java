package techquack.com.onestar3gram.DTO;

import lombok.Getter;
import lombok.Setter;


public class EditPostCommand {

    @Setter
    @Getter
    private String alt;

    @Setter
    @Getter
    private String description;
    private Boolean _private;

    public Boolean isPrivate() { return _private; }

    public void setPrivate(boolean aPrivate) { _private = aPrivate; }
}
