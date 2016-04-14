package jp.cordea.switter.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Yoshihiro Tanaka on 16/04/14.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LocalRetweet extends RealmObject {
    @PrimaryKey
    private long tweetId;

    @Required
    private String text;

    @Required
    private String name;

    @Required
    private String screenName;

    @Required
    private String profileImageUrl;
}
