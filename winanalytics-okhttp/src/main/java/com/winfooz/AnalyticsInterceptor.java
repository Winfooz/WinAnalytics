package com.winfooz;

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

    private String baseUrl;

    public AnalyticsInterceptor(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        try {
            Response response = chain.proceed(chain.request());
            if (response.isSuccessful()) {
                WinAnalytics.getInstance().logSuccess(baseUrl, chain.request().url().toString());
            } else {
                WinAnalytics.getInstance().logFailure(baseUrl, chain.request().url().toString());
            }
            return response;
        } catch (IOException e) {
            WinAnalytics.getInstance().logFailure(baseUrl, chain.request().url().toString());
            throw e;
        }
    }
}
