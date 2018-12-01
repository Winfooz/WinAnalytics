package com.winfooz;

import android.support.annotation.NonNull;
import java.lang.reflect.Type;
import retrofit2.Call;
import retrofit2.CallAdapter;

public class AnalyticsCallAdapter<R> implements CallAdapter<R, Call<R>> {

    private final Type type;
    private final String baseUrl;
    private final boolean logEvent;

    AnalyticsCallAdapter(Type type, String baseUrl, boolean logEvent) {
        this.type = type;
        this.baseUrl = baseUrl;
        this.logEvent = logEvent;
    }

    @NonNull
    @Override
    public Type responseType() {
        return type;
    }

    @NonNull
    @Override
    public Call<R> adapt(@NonNull Call<R> call) {
        return new AnalyticsEnqueueAdapter<>(baseUrl, call, logEvent);
    }
}
