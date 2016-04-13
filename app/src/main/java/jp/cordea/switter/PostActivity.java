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
import io.realm.Realm;
import jp.cordea.switter.realm.LocalTweet;

public class PostActivity extends AppCompatActivity {

    private static final String POST_TYPE_KEY = "PostTypeKey";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.fab)
    FloatingActionButton button;

    @Bind(R.id.edit_text)
    EditText editText;

    public static Intent creaateIntent(Context context, PostType type) {
        // TODO: receive user data
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra(POST_TYPE_KEY, type.toString());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        setSupportActionBar(toolbar);

        PostType type = PostType.valueOf(getIntent().getStringExtra(POST_TYPE_KEY));

        // TODO: insert text

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable editable = editText.getText();
                if (editable != null && editable.length() > 0) {
                    final String text = editable.toString();
                    // TODO
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            LocalTweet localTweet = realm.createObject(LocalTweet.class);
                            localTweet.setEpoch(System.currentTimeMillis());
                            localTweet.setText(text);
                        }
                    });
                }
            }
        });
    }

}
