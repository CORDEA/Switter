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

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.twitter_login_button)
    TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        final Context context = this;

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                TwitterSession session = result.data;
                // TODO: Remove toast and use the TwitterSession's userID
                // with your app's user model
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });


        addDemo(R.id.demo1, "hogehoge", "_Cordea");
        addDemo(R.id.demo2, "hogehoge", "_Cordea");
        addDemo(R.id.demo3, "hogehoge", "_Cordea");
        addDemo(R.id.demo4, "hogehoge", "_Cordea");
        addDemo(R.id.demo5, "hogehoge", "_Cordea");
        addDemo(R.id.demo6, "hogehoge", "_Cordea");
    }

    private void addDemo(int id, String text, String username) {
        View view = findViewById(id);
        assert view != null;

        TextView nameTextView = (TextView) view.findViewById(R.id.user_name);
        TextView dateTextView = (TextView) view.findViewById(R.id.date);
        TextView contentTextView = (TextView) view.findViewById(R.id.content);

        nameTextView.setText(username);
        contentTextView.setText(text);
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
