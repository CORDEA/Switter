package jp.cordea.switter.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Yoshihiro Tanaka on 16/04/05.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LocalTweet extends RealmObject {

    @PrimaryKey
    private long id;

    private long tweetId;

    private long epoch;

    @Required
    private String text;

    private String profileImageUrl;

    private long userId;

    private String userName;

    private String userScreenName;

    private boolean isReply;

    private boolean isFavorite;

    private boolean isRetweet;

    private long replyTweetId;

    private int favoriteCount;

    private int retweetCount;

    private RealmList<LocalEntity> entities;

    public String getProfileBiggerImageUrl() {
        return profileImageUrl.replace("normal.png", "bigger.png");
    }
}
