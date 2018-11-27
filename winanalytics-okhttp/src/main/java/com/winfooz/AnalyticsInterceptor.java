package com.winfooz;

import android.os.Handler;
import android.support.annotation.NonNull;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Project: WinAnalytics2 Created: November 24, 2018
 *
 * @author Mohamed Hamdan
 */
public class AnalyticsInterceptor implements Interceptor {

    private static final int DELAYED = 2000;

    private String baseUrl;
    private Handler handler = new Handler();

    public AnalyticsInterceptor(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        try {
            Response response = chain.proceed(chain.request());
            if (response.isSuccessful()) {
                handler.postDelayed(
                        () ->
                                WinAnalytics.getInstance()
                                        .logSuccess(baseUrl, chain.request().url().toString()),
                        DELAYED);
            } else {
                handler.postDelayed(
                        () ->
                                WinAnalytics.getInstance()
                                        .logFailure(baseUrl, chain.request().url().toString()),
                        DELAYED);
            }
            return response;
        } catch (IOException e) {
            handler.postDelayed(
                    () ->
                            WinAnalytics.getInstance()
                                    .logFailure(baseUrl, chain.request().url().toString()),
                    DELAYED);
            throw e;
        }
    }
}
