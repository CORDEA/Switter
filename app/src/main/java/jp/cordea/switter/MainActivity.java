package jp.cordea.switter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import jp.cordea.switter.realm.LocalTweet;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fab)
    FloatingActionButton button;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.list_view)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        final Context context = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = PostActivity.createIntent(context, PostType.Tweet, -1);
                startActivity(intent);
            }
        });

        final MainListAdapter adapter = new MainListAdapter(this, new ArrayList<LocalTweet>());

        TwitterApiClient client = TwitterCore.getInstance().getApiClient();
        StatusesService service = client.getStatusesService();
        service.homeTimeline(50, null, null, false, false, false, false, new Callback<List<Tweet>>() {
            @Override
            public void success(final Result<List<Tweet>> result) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                List<Tweet> tweets = result.data;
                for (int i = 0; i < tweets.size(); i++) {
                    Tweet tweet = tweets.get(i);
                    if (realm.where(LocalTweet.class).equalTo("tweetId", tweet.id).count() == 0) {
                        LocalTweet localTweet = realm.createObject(LocalTweet.class);
                        DateTime dateTime = parseTwitterDate(tweet.createdAt);
                        localTweet.setEpoch(dateTime.getMillis());
                        localTweet.setText(tweet.text);
                        localTweet.setTweetId(tweet.id);
                        localTweet.setUserId(tweet.user.id);
                        localTweet.setUserName(tweet.user.name);
                        localTweet.setUserScreenName(tweet.user.screenName);
                        localTweet.setFavoriteCount(tweet.favoriteCount);
                        localTweet.setRetweetCount(tweet.retweetCount);
                        localTweet.setProfileImageUrl(tweet.user.profileImageUrl);
                    }
                }
                realm.commitTransaction();
                realm.close();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });

        listView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private DateTime parseTwitterDate(String twitterDate) {
        return DateTime.parse(twitterDate, DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss Z yyyy").withLocale(Locale.ENGLISH));
    }
}
