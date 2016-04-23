package jp.cordea.switter;

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

        final MainListAdapter adapter = new MainListAdapter(this, new ArrayList<Tweet>());

        getTweets(adapter, 50);

        listView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTweets(adapter, 10);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getTweets(final MainListAdapter adapter, int count) {
        TwitterApiClient client = TwitterCore.getInstance().getApiClient();
        StatusesService service = client.getStatusesService();
        service.homeTimeline(count, null, null, false, false, false, false, new Callback<List<Tweet>>() {
            @Override
            public void success(final Result<List<Tweet>> result) {
                Realm realm = Realm.getDefaultInstance();
                List<LocalTweet> localTweets = realm.copyFromRealm(realm.allObjects(LocalTweet.class));
                List<Tweet> tweets = new ArrayList<>();
                tweets.addAll(result.data);
                for (int i = 0; i < localTweets.size(); i++) {
                    tweets.add(LocalTweet.toTweet(localTweets.get(i)));
                }
                realm.close();
                adapter.insertItems(tweets);
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });
    }
}
