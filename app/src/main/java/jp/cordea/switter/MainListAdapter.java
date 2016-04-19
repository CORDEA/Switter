package jp.cordea.switter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import jp.cordea.switter.realm.Favorite;
import jp.cordea.switter.realm.LocalRetweet;
import jp.cordea.switter.realm.LocalTweet;

/**
 * Created by Yoshihiro Tanaka on 16/03/30.
 */
public class MainListAdapter extends ArrayAdapter<LocalTweet> {

    private List<LocalTweet> tweets;

    public MainListAdapter(Context context, List<LocalTweet> tweets) {
        super(context, R.layout.main_list_item, tweets);
        this.tweets = tweets;
    }

    @Override
    public void notifyDataSetChanged() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<LocalTweet> realmResults = realm.where(LocalTweet.class).findAllSorted("epoch", Sort.DESCENDING);
        tweets.clear();
        tweets.addAll(realm.copyFromRealm(realmResults));
        super.notifyDataSetChanged();
    }

    @Override
    public LocalTweet getItem(int position) {
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

        final ImageView favoriteButton = (ImageView) view.findViewById(R.id.favorite_button);
        final TextView favoriteTextView = (TextView) view.findViewById(R.id.favorite_text_view);
        final ImageView retweetButton = (ImageView) view.findViewById(R.id.retweet_button);
        TextView retweetTextView = (TextView) view.findViewById(R.id.retweet_text_view);
        ImageView replyButton = (ImageView) view.findViewById(R.id.reply_button);

        int color = ContextCompat.getColor(getContext(), R.color.colorPrimaryText);
        int secColor = ContextCompat.getColor(getContext(), R.color.colorSecondaryText);

        float size = getContext().getResources().getDimension(R.dimen.user_name_text_size);
        float secSize = getContext().getResources().getDimension(R.dimen.user_id_text_size);

        final float thumbnailRound = getContext().getResources().getDimension(R.dimen.user_thumbnail_round);

        final LocalTweet tweet = tweets.get(position);
        String string = tweet.getUserName() + " @" + tweet.getUserScreenName();
        Picasso.with(getContext())
                .load(tweet.getProfileBiggerImageUrl())
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
        SpannableString spannableString = new SpannableString(string);
        int length = tweet.getUserName().length();
        spannableString.setSpan(new AbsoluteSizeSpan((int) size), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan((int) secSize), length + 1, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(secColor), length + 1, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        nameTextView.setText(spannableString);
        DateTime time = new DateTime(tweet.getEpoch());
        DateTime now = new DateTime();
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
        contentTextView.setText(tweet.getText());

        int favorites = tweet.getFavoriteCount();
        int retweets = tweet.getRetweetCount();

        Realm realm = Realm.getDefaultInstance();
        boolean isFavorite = realm.where(Favorite.class).equalTo("tweetId", tweet.getTweetId()).count() != 0;
        boolean isRetweet = realm.where(LocalRetweet.class).equalTo("tweetId", tweet.getTweetId()).count() != 0;
        realm.close();

        if (isFavorite) {
            ++favorites;
        } else {
            favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm realm = Realm.getDefaultInstance();
                LocalTweet localTweet = realm.where(LocalTweet.class).equalTo("tweetId", tweet.getTweetId()).findFirst();
                if (!localTweet.isFavorite()) {
                    realm.beginTransaction();
                    localTweet.setFavoriteCount(localTweet.getFavoriteCount() + 1);
                    localTweet.setFavorite(true);
                    realm.copyToRealmOrUpdate(localTweet);
                    realm.commitTransaction();
                    favoriteTextView.setText(String.format("%d", Integer.parseInt(favoriteTextView.getText().toString()) + 1));
                    favoriteButton.setEnabled(false);
                }
                realm.close();
            }
        });
        }

        if (isRetweet) {
            ++retweets;
        } else {
            retweetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Realm realm = Realm.getDefaultInstance();
                    LocalTweet localTweet = realm.where(LocalTweet.class).equalTo("tweetId", tweet.getTweetId()).findFirst();
                    if (!localTweet.isRetweet()) {
                        realm.beginTransaction();
                        localTweet.setRetweetCount(localTweet.getRetweetCount() + 1);
                        localTweet.setRetweet(true);
                        realm.copyToRealmOrUpdate(localTweet);
                        realm.commitTransaction();
                        retweetButton.setEnabled(false);
                    }
                    realm.close();
                    // TODO
                }
            });
        }
        realm.close();

        favoriteTextView.setText(String.format("%d", favorites));
        retweetTextView.setText(String.format("%d", retweets));

        replyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = PostActivity.createIntent(getContext(), PostType.Reply, tweet.getTweetId());
                getContext().startActivity(intent);
            }
        });

        return view;
    }
}
