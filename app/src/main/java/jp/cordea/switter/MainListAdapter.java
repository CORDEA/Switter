package jp.cordea.switter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        TextView nameTextView = (TextView) view.findViewById(R.id.user_name);
        TextView idTextView = (TextView) view.findViewById(R.id.user_id);
        TextView dateTextView = (TextView) view.findViewById(R.id.date);
        TextView contentTextView = (TextView) view.findViewById(R.id.content);

        ImageView moreButton = (ImageView) view.findViewById(R.id.more_button);
        ImageView starButton = (ImageView) view.findViewById(R.id.star_button);
        ImageView retweetButton = (ImageView) view.findViewById(R.id.retweet_button);
        ImageView replyButton = (ImageView) view.findViewById(R.id.reply_button);

        final Tweet tweet = tweets.get(position);
        nameTextView.setText(tweet.user.name);
        idTextView.setText(tweet.user.screenName);
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
