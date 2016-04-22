package jp.cordea.switter.realm;

import io.realm.RealmObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Yoshihiro Tanaka on 16/04/21.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LocalEntity extends RealmObject {

    public LocalEntity() {
    }

    public LocalEntity(String type, String displayUrl, String mediaUrl) {
        this.type = type;
        this.displayUrl = displayUrl;
        this.mediaUrl = mediaUrl;
    }

    private String type;

    private String displayUrl;

    private String mediaUrl;

}
