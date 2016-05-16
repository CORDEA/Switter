package jp.cordea.switter;

import com.twitter.sdk.android.core.models.Tweet;

import org.parceler.Parcel;

/**
 * Created by Yoshihiro Tanaka on 16/05/16.
 */
@Parcel
public class ParcelableTweet {

    public ParcelableTweet() {
    }

    public ParcelableTweet(long tweetId, String text, String userName, String userScreenName) {
        this.tweetId = tweetId;
        this.text = text;
        this.userName = userName;
        this.userScreenName = userScreenName;
    }

    long tweetId;
    String text;
    String userName;
    String userScreenName;

    public long getTweetId() {
        return tweetId;
    }

    public String getText() {
        return text;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserScreenName() {
        return userScreenName;
    }

    public static ParcelableTweet fromTweet(Tweet tweet) {
        return new ParcelableTweet(tweet.id, tweet.text, tweet.user.name, tweet.user.screenName);
    }
}
