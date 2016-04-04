package jp.cordea.switter.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Yoshihiro Tanaka on 16/04/05.
 */
public class Tweet extends RealmObject {
    @PrimaryKey
    private long epoch;

    @Required
    private String text;
    private String createdAt;
    private boolean isRetweet;
}
