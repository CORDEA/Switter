package jp.cordea.switter.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yoshihiro Tanaka on 16/04/05.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LocalTweet extends RealmObject {
    @PrimaryKey
    private long epoch;

    @Required
    private String text;

    private String createdAt;
    private String name;
    private String screenName;
    private String profileImageUrl;

    private boolean isRetweet;
    private boolean isReply;
}
