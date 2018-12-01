package com.winfooz;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import java.io.IOException;
import kotlin.Unit;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalyticsEnqueueAdapter<T> implements Call<T> {

    private final Handler handler = new Handler(Looper.getMainLooper());

    private final Call<T> call;
    private final String baseUrl;
    private final boolean logEvent;

    AnalyticsEnqueueAdapter(String baseUrl, Call<T> call, boolean logEvent) {
        this.call = call;
        this.baseUrl = baseUrl;
        this.logEvent = logEvent;
    }

    @Override
    public void enqueue(@NonNull Callback<T> callback) {
        call.enqueue(
                new Callback<T>() {
                    @Override
                    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                        if (logEvent) {
                            WinAnalytics.getInstance()
                                    .initEventArguments(
                                            baseUrl,
                                            call.request().url().toString(),
                                            response,
                                            true,
                                            () -> response(callback, response));
                        } else {
                            response(callback, response);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                        if (logEvent) {
                            WinAnalytics.getInstance()
                                    .initEventArguments(
                                            baseUrl,
                                            call.request().url().toString(),
                                            null,
                                            true,
                                            () -> failure(callback, t));
                        } else {
                            failure(callback, t);
                        }
                    }
                });
    }

    @NonNull
    @Override
    public Response<T> execute() throws IOException {
        return call.execute();
    }

    @Override
    public boolean isExecuted() {
        return call.isExecuted();
    }

    @Override
    public void cancel() {
        call.cancel();
    }

    @Override
    public boolean isCanceled() {
        return call.isCanceled();
    }

    @NonNull
    @Override
    public Call<T> clone() {
        return call.clone();
    }

    @NonNull
    @Override
    public Request request() {
        return call.request();
    }

    private Unit response(@NonNull Callback<T> callback, @NonNull Response<T> response) {
        if (logEvent) {
            if (response.isSuccessful()) {
                WinAnalytics.getInstance().logSuccess(baseUrl, call.request().url().toString());
            } else {
                WinAnalytics.getInstance().logFailure(baseUrl, call.request().url().toString());
            }
        }
        handler.post(() -> callback.onResponse(call, response));

        return Unit.INSTANCE;
    }

    private Unit failure(@NonNull Callback<T> callback, @NonNull Throwable t) {
        if (logEvent) {
            WinAnalytics.getInstance().logFailure(baseUrl, call.request().url().toString());
        }
        handler.post(() -> callback.onFailure(call, t));

        return Unit.INSTANCE;
    }
}
