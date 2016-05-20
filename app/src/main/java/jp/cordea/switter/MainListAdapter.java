package jp.cordea.switter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.twitter.sdk.android.core.models.Tweet;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import jp.cordea.switter.realm.LocalFavorite;
import jp.cordea.switter.realm.LocalRetweet;
import jp.cordea.switter.realm.LocalTweet;

/**
 * Created by Yoshihiro Tanaka on 16/03/30.
 */
public class MainListAdapter extends ArrayAdapter<Tweet> {

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
        Collections.sort(this.tweets, new Comparator<Tweet>() {
            @Override
            public int compare(Tweet tweet, Tweet t1) {
                long d = LocalTweet.twitterDateToEpoch(tweet.createdAt) - LocalTweet.twitterDateToEpoch(t1.createdAt);
                if (d == 0) {
                    return 0;
                }
                if (d > 0) {
                    return -1;
                }
                return 1;
            }
        });

        notifyDataSetChanged();
    }

    private List<Tweet> tweets;

    private final int postRequestCode;
    private Activity activity;

    public MainListAdapter(Activity activity, List<Tweet> tweets, int postRequestCode) {
        super(activity, R.layout.main_list_item, tweets);
        this.tweets = tweets;
        this.activity = activity;
        this.postRequestCode = postRequestCode;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Tweet getItem(int position) {
        return tweets.get(position);
    }

    @Override
    public int getCount() {
        return tweets.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView == null ? LayoutInflater.from(getContext()).inflate(R.layout.main_list_item, null, false) : convertView;

        ImageView userImageView = (ImageView) view.findViewById(R.id.user_thumbnail);
        TextView nameTextView = (TextView) view.findViewById(R.id.user_name);
        TextView dateTextView = (TextView) view.findViewById(R.id.date);
        TextView contentTextView = (TextView) view.findViewById(R.id.content);
        TimelineImageView timelineImageView = (TimelineImageView) view.findViewById(R.id.timeline_image_view);
        View retweetNotifyContainer = view.findViewById(R.id.retweet_notify_container);
        TextView retweetNotifyTextView = (TextView) view.findViewById(R.id.retweet_notify_text_view);

        final ImageView favoriteButton = (ImageView) view.findViewById(R.id.favorite_button);
        final TextView favoriteTextView = (TextView) view.findViewById(R.id.favorite_text_view);
        final ImageView retweetButton = (ImageView) view.findViewById(R.id.retweet_button);
        final TextView retweetTextView = (TextView) view.findViewById(R.id.retweet_text_view);
        ImageView replyButton = (ImageView) view.findViewById(R.id.reply_button);

        String retweetNotifyFormatText = getContext().getResources().getString(R.string.retweet_notify_format_text);

        final float thumbnailRound = getContext().getResources().getDimension(R.dimen.user_thumbnail_round);

        Tweet tweet = tweets.get(position);

        if (tweet.retweetedStatus == null) {
            retweetNotifyContainer.setVisibility(View.GONE);
        } else {
            retweetNotifyContainer.setVisibility(View.VISIBLE);
            retweetNotifyTextView.setText(String.format(retweetNotifyFormatText, tweet.user.name));
            tweet = tweet.retweetedStatus;
        }

        Picasso.with(getContext())
                .load(tweet.user.profileImageUrl)
                .transform(new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {
                        int size = source.getWidth();

                        Bitmap out = Bitmap.createBitmap(size, size, source.getConfig());
                        Canvas canvas = new Canvas(out);

                        Paint paint = new Paint();
                        BitmapShader shader = new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
                        paint.setShader(shader);
                        paint.setAntiAlias(true);

                        Rect rect = new Rect(0, 0, size, size);
                        RectF rectF = new RectF(rect);
                        canvas.drawRoundRect(rectF, thumbnailRound, thumbnailRound, paint);

                        source.recycle();

                        return out;
                    }

                    @Override
                    public String key() {
                        return "round";
                    }
                })
                .into(userImageView);

        nameTextView.setText(TimeLineUtils.getSpannableStringOfName(getContext(), tweet.user.name, tweet.user.screenName));
        DateTime time = new DateTime(LocalTweet.twitterDateToEpoch(tweet.createdAt));
        DateTime now = new DateTime();
        // FIXME
        Duration duration = new Interval(time, now).toDuration();
        long display = duration.getStandardMinutes();
        int format = R.string.minute_format;
        if (display >= 60) {
            format = R.string.hour_format;
            display = duration.getStandardHours();
            if (display >= 60) {
                format = R.string.day_format;
                display = duration.getStandardDays();
            }
        }

        dateTextView.setText(String.format(getContext().getResources().getString(format), display));

        if (tweet.entities != null  && tweet.entities.media != null && tweet.entities.media.size() > 0) {
            timelineImageView.setImages(tweet.entities.media);
        } else {
            timelineImageView.setVisibility(View.GONE);
        }
        contentTextView.setText(tweet.text);

        int favorites = tweet.favoriteCount;
        int retweets = tweet.retweetCount;

        boolean isFavorite = false;
        boolean isRetweet = false;
        if (tweet.id != -1) {
            Realm realm = Realm.getDefaultInstance();
            isFavorite = realm.where(LocalFavorite.class).equalTo("tweetId", tweet.id).count() != 0;
            isRetweet = realm.where(LocalRetweet.class).equalTo("tweetId", tweet.id).count() != 0;
            realm.close();
        }

        final Tweet finalTweet = tweet;

        if (isFavorite) {
            ++favorites;
            favoriteButton.setEnabled(false);
        } else {
            if (tweet.id == -1) {
                favoriteButton.setEnabled(false);
            } else {
                favoriteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Realm realm = Realm.getDefaultInstance();
                        realm.beginTransaction();
                        LocalFavorite favorite = realm.createObject(LocalFavorite.class);
                        favorite.setTweetId(finalTweet.id);
                        favorite.setUserId(finalTweet.user.id);
                        realm.commitTransaction();
                        favoriteTextView.setText(String.format(Locale.getDefault(), "%d",
                                Integer.parseInt(favoriteTextView.getText().toString()) + 1));
                        favoriteButton.setEnabled(false);
                        realm.close();
                    }
                });
            }
        }

        if (isRetweet) {
            ++retweets;
            retweetButton.setEnabled(false);
        } else {
            if (tweet.id == -1) {
                retweetButton.setEnabled(false);
            } else {
                retweetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(getContext())
                        .setItems(R.array.retweets, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == 0) {
                                    Realm realm = Realm.getDefaultInstance();
                                    realm.beginTransaction();
                                    LocalRetweet retweet = realm.createObject(LocalRetweet.class);
                                    retweet.setTweetId(finalTweet.id);
                                    retweet.setText(finalTweet.text);
                                    retweet.setName(finalTweet.user.name);
                                    retweet.setScreenName(finalTweet.user.screenName);
                                    retweet.setProfileImageUrl(finalTweet.user.profileImageUrl);
                                    realm.commitTransaction();
                                    realm.close();
                                    retweetTextView.setText(String.format(Locale.getDefault(), "%d",
                                            Integer.parseInt(retweetTextView.getText().toString()) + 1));
                                    retweetButton.setEnabled(false);
                                } else {
                                    Intent intent = PostActivity.createIntent(getContext(), PostType.Retweet, ParcelableTweet.fromTweet(finalTweet));
                                    activity.startActivityForResult(intent, postRequestCode);
                                }
                            }
                        })
                        .show();
                    }
                });
            }
        }

        favoriteTextView.setText(String.format(Locale.getDefault(), "%d", favorites));
        retweetTextView.setText(String.format(Locale.getDefault(), "%d", retweets));

        if (tweet.id == -1) {
            replyButton.setEnabled(false);
        } else {
            replyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = PostActivity.createIntent(getContext(), PostType.Reply, ParcelableTweet.fromTweet(finalTweet));
                    activity.startActivityForResult(intent, postRequestCode);
                }
            });
        }

        return view;
    }
}
