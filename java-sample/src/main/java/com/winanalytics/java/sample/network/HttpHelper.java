package com.winanalytics.java.sample.network;

import com.winfooz.AnalyticsInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Project: WinAnalytics2
 * Created: November 23, 2018
 *
 * @author Mohamed Hamdan
 */
public class HttpHelper {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    public static HttpClient getHttpClient() {
        return new Retrofit.Builder()
                .client(
                        new OkHttpClient.Builder()
                                .addInterceptor(new AnalyticsInterceptor(BASE_URL))
                                .build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(HttpClient.class);
    }
}
