package jp.cordea.switter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import jp.cordea.switter.realm.ActiveUser;
import jp.cordea.switter.realm.LocalTweet;

public class PostActivity extends AppCompatActivity {

    private static final String POST_TYPE_KEY = "PostTypeKey";
    private static final String REPLY_TWEET_ID_KEY = "ReplyTweetIdKey";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fab)
    FloatingActionButton button;

    @Bind(R.id.edit_text)
    EditText editText;

    public static Intent createIntent(Context context, PostType type, long replyTweetId) {
        // TODO: receive user data
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra(POST_TYPE_KEY, type.toString());
        intent.putExtra(REPLY_TWEET_ID_KEY, replyTweetId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        final PostType type = PostType.valueOf(getIntent().getStringExtra(POST_TYPE_KEY));
        final long replyTweetId = getIntent().getLongExtra(REPLY_TWEET_ID_KEY, -1);

        // TODO: insert text

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable editable = editText.getText();
                if (editable != null && editable.length() > 0) {
                    saveTweet(editable.toString(), type, replyTweetId);
                }
                finish();
            }
        });
    }

    private void saveTweet(String text, PostType type, long replyTweetId) {
        Realm realm = Realm.getDefaultInstance();
        ActiveUser user = realm.where(ActiveUser.class).findFirst();
        Number id = realm.allObjects(LocalTweet.class).max("id");
        realm.beginTransaction();
        LocalTweet localTweet = realm.createObject(LocalTweet.class);
        switch (type) {
            case Reply:
                localTweet.setReply(true);
                localTweet.setReplyTweetId(replyTweetId);
                break;
        }

        // FIXME
        localTweet.setId(id == null ? 1 : id.longValue() + 1);
        localTweet.setCreatedAt(LocalTweet.epochToString(System.currentTimeMillis()));
        localTweet.setTweetId(-1);
        localTweet.setText(text);
        localTweet.setUserName(user.getName());
        localTweet.setUserScreenName(user.getScreenName());
        localTweet.setProfileImageUrl(user.getProfileImageUrl());
        localTweet.setFavoriteCount(0);
        localTweet.setRetweetCount(0);
        localTweet.setUserId(user.getId());

        realm.commitTransaction();
        realm.close();
    }
}
