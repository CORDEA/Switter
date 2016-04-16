package jp.cordea.switter;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.models.User;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Yoshihiro Tanaka on 16/04/17.
 */
public class UsersApiClient extends TwitterApiClient {

    public UsersApiClient(Session session) {
        super(session);
    }

    public UsersService getUsersService() {
        return getService(UsersService.class);
    }
}

interface UsersService {
    @GET("/1.1/users/show.json")
    void show(@Query("user_id") long id, Callback<User> cb);
}
