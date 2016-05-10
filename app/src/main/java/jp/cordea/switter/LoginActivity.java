package jp.cordea.switter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import jp.cordea.switter.realm.ActiveUser;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.twitter_login_button)
    TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        final Context context = this;

        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        if (session == null) {
            loginButton.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    TwitterSession session = result.data;
                    getOwnData(session);
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.d("TwitterKit", "Login with Twitter failure", exception);
                }
            });
        } else {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            getOwnData(session);
        }
    }

    private void getOwnData(TwitterSession session) {
        UsersApiClient client = new UsersApiClient(session);
        UsersService service = client.getUsersService();
        service.show(session.getUserId(), new Callback<User>() {
            @Override
            public void success(final Result<User> result) {
                Realm realm = Realm.getDefaultInstance();
                final User user = result.data;
                if (realm.where(ActiveUser.class).equalTo("id", user.id).count() == 0) {
                    realm.beginTransaction();
                    ActiveUser activeUser = realm.createObject(ActiveUser.class);
                    activeUser.setProfileImageUrl(user.profileImageUrl);
                    activeUser.setId(user.id);
                    activeUser.setScreenName(user.screenName);
                    activeUser.setName(user.name);
                    realm.commitTransaction();
                }
                realm.close();
            }

            @Override
            public void failure(TwitterException e) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
