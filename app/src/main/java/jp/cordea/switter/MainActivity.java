package jp.cordea.switter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import jp.cordea.switter.realm.LocalTweet;

public class MainActivity extends AppCompatActivity {

    private static final int POST_REQUEST_CODE = 100;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fab)
    FloatingActionButton button;

    @Bind(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.list_view)
    ListView listView;

    private MainListAdapter adapter;

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
                Intent intent = PostActivity.createIntent(context, PostType.Tweet, null);
                startActivityForResult(intent, POST_REQUEST_CODE);
            }
        });

        adapter = new MainListAdapter(this, new ArrayList<Tweet>(), POST_REQUEST_CODE);

        getTweets(adapter, 50);

        listView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == POST_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                swipeRefreshLayout.setRefreshing(true);
                refresh();
            }
        }
    }

    private void refresh() {
        getTweets(adapter, 50);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void getTweets(final MainListAdapter adapter, int count) {
        TwitterApiClient client = TwitterCore.getInstance().getApiClient();
        StatusesService service = client.getStatusesService();
        service.homeTimeline(count, null, null, false, false, false, true, new Callback<List<Tweet>>() {
            @Override
            public void success(final Result<List<Tweet>> result) {
                Realm realm = Realm.getDefaultInstance();
                List<LocalTweet> localTweets = realm.copyFromRealm(realm.allObjects(LocalTweet.class));
                List<Tweet> tweets = new ArrayList<>();
                if (result.data.size() > 0) {
                    tweets.addAll(result.data);
                    long minEpoch = LocalTweet.twitterDateToEpoch(result.data.get(0).createdAt);
                    for (int i = 0; i < result.data.size(); i++) {
                        Tweet tweet = result.data.get(i);
                        long epoch = LocalTweet.twitterDateToEpoch(tweet.createdAt);
                        if (minEpoch > epoch) {
                            minEpoch = epoch;
                        }
                    }
                    for (int i = 0; i < localTweets.size(); i++) {
                        if (minEpoch <= LocalTweet.twitterDateToEpoch(localTweets.get(i).getCreatedAt())) {
                            tweets.add(localTweets.get(i).toTweet());
                        }
                    }
                }
                realm.close();
                adapter.setTweets(tweets);
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });
    }
}
