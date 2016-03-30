package jp.cordea.switter.api;

import java.util.List;

import jp.cordea.switter.api.response.HomeTimeline;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Yoshihiro Tanaka on 16/03/31.
 */
public interface TwitterApi {

    @GET("/statuses/home_timeline.json")
    public Call<List<HomeTimeline>> getHomeTimeline (
            @Query("count") int count,
            @Query("since_id") int sinceId,
            @Query("max_id") int maxId,
            @Query("trim_user") boolean isTrimUser,
            @Query("exclude_replies") boolean isExcludeReplies,
            @Query("contributor_details") boolean isContributorDetails,
            @Query("include_entities") boolean isIncludeEntities
    );

}
