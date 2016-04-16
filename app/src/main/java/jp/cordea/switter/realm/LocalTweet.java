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

    private long tweetId;

    private long epoch;

    @Required
    private String text;

    private String profileImageUrl;

    private long userId;

    private String userName;

    private String userScreenName;

    private boolean isReply;

    private long replyTweetId;

    private int favoriteCount;

    private int retweetCount;
}
