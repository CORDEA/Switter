package jp.cordea.switter.realm;

import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.UrlEntity;
import com.twitter.sdk.android.core.models.User;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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

    private String createdAt;

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

    public static Tweet toTweet(LocalTweet localTweet) {
        RealmList<LocalEntity> entities = localTweet.getEntities();
        List<UrlEntity> urlEntities = new ArrayList<>();
        List<MediaEntity> mediaEntities = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            LocalEntity entity = entities.get(i);
            if (entity.getType().equals("url")) {
                urlEntities.add(new UrlEntity(entity.getMediaUrl(), null, entity.getDisplayUrl(), 0, 0));
            } else {
                mediaEntities.add(
                        new MediaEntity(null, null, entity.getDisplayUrl(), 0, 0, 0, null, entity.getMediaUrl(), null, null, 0, null, null, null));
            }
        }

        return new Tweet(
                null,
                localTweet.getCreatedAt(),
                null,
                new TweetEntities(urlEntities, null, mediaEntities, null),
                null,
                localTweet.getFavoriteCount(),
                localTweet.isFavorite(),
                null,
                localTweet.getTweetId(),
                null,
                null,
                0,
                null,
                0,
                null,
                null,
                null,
                false,
                null,
                localTweet.getRetweetCount(),
                localTweet.isRetweet(),
                null,
                null,
                localTweet.getText(),
                false,
                new User(false, null, false, false, null, null, null, 0, false, 0, 0, false, localTweet.getUserId(), null, false, null, 0, null, localTweet.getUserName(),
                        null, null, null, false, null, localTweet.getProfileImageUrl(), null, null, null, null, null, false,
                        false, localTweet.getUserScreenName(), false, null, 0, null, null, 0 , false, null, null),
                false,
                null,
                null);
    }

    public static String epochToString(long epoch) {
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
        return format.format(new Date(epoch));
    }

    public static long twitterDateToEpoch(String twitterDate) {
        return DateTime.parse(twitterDate, DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss Z yyyy").withLocale(Locale.ENGLISH)).getMillis();
    }
}
