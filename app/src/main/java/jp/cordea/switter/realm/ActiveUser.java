package jp.cordea.switter.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Yoshihiro Tanaka on 16/04/17.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActiveUser extends RealmObject {

    @PrimaryKey
    private long id;

    private String name;

    private String screenName;

    private String profileImageUrl;
}
