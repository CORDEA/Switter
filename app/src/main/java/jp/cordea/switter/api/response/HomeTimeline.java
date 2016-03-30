package jp.cordea.switter.api.response;

import java.util.List;

/**
 * Created by Yoshihiro Tanaka on 16/03/31.
 */
public class HomeTimeline {
    String createdAt;
    String text;
    long id;
    int retweetCount;
    int favouritesCount;
    String profileImageUrlHttps;
    User user;
}
