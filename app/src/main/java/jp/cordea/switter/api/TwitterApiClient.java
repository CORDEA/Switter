package jp.cordea.switter.api;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Yoshihiro Tanaka on 16/03/31.
 */
public class TwitterApiClient {

    private static final String BASE_URL = "https://api.twitter.com/1.1";

    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
}
