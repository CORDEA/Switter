package jp.cordea.switter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

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
        nameTextView.setText(tweet.user.screenName);
        idTextView.setText(tweet.user.idStr);
        dateTextView.setText(tweet.createdAt);
        contentTextView.setText(tweet.text);

        return view;
    }
}
