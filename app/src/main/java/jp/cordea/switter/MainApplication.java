package jp.cordea.switter;

import android.app.Application;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by CORDEA on 2016/04/03.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
    }
}
