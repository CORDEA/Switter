package jp.cordea.switter;

import android.content.Context;
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
import com.twitter.sdk.android.core.models.Tweet;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;

import java.util.List;
import java.util.Locale;

/**
 * Created by Yoshihiro Tanaka on 16/03/30.
 */
public class MainListAdapter extends ArrayAdapter<Tweet> {

    private List<Tweet> tweets;

    public MainListAdapter(Context context, List<Tweet> tweets) {
        super(context, R.layout.main_list_item, tweets);
        this.tweets = tweets;
    }

    public void addItems(List<Tweet> tweets) {
        this.tweets.addAll(tweets);
        notifyDataSetChanged();
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

        ImageView moreButton = (ImageView) view.findViewById(R.id.more_button);
        ImageView starButton = (ImageView) view.findViewById(R.id.star_button);
        ImageView retweetButton = (ImageView) view.findViewById(R.id.retweet_button);
        ImageView replyButton = (ImageView) view.findViewById(R.id.reply_button);

        int color = ContextCompat.getColor(getContext(), R.color.colorPrimaryText);
        int secColor = ContextCompat.getColor(getContext(), R.color.colorSecondaryText);

        float size = getContext().getResources().getDimension(R.dimen.user_name_text_size);
        float secSize = getContext().getResources().getDimension(R.dimen.user_id_text_size);

        final float thumbnailRound = getContext().getResources().getDimension(R.dimen.user_thumbnail_round);

        final Tweet tweet = tweets.get(position);
        String string = tweet.user.name + " @" + tweet.user.screenName;
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
        SpannableString spannableString = new SpannableString(string);
        int length = tweet.user.name.length();
        spannableString.setSpan(new AbsoluteSizeSpan((int) size), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan((int) secSize), length + 1, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(secColor), length + 1, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        nameTextView.setText(spannableString);
        DateTime time = parseTwitterDate(tweet.createdAt);
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
        contentTextView.setText(tweet.text);

        return view;
    }

    private DateTime parseTwitterDate(String twitterDate) {
        return DateTime.parse(twitterDate, DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss Z yyyy").withLocale(Locale.ENGLISH));
    }
}
