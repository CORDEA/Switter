package jp.cordea.switter.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Yoshihiro Tanaka on 16/04/13.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Favorite extends RealmObject {
    @PrimaryKey
    private long tweetId;

    private long userId;
}
