package jp.cordea.switter.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Yoshihiro Tanaka on 16/04/13.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Favorite extends RealmObject {
    @PrimaryKey
    long tweetId;

    long userId;
}
