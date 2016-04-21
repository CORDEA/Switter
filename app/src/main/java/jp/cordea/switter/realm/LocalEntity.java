package jp.cordea.switter.realm;

import io.realm.RealmObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Yoshihiro Tanaka on 16/04/21.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LocalEntity extends RealmObject {

    private String type;

    private String displayUrl;

    private String mediaUrl;

}
