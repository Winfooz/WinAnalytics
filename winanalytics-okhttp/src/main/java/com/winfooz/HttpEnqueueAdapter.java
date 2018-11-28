package com.winfooz;

import android.support.annotation.NonNull;
import java.io.IOException;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HttpEnqueueAdapter<T> implements Call<T> {

    private final Call<T> call;
    private String baseUrl;

    HttpEnqueueAdapter(String baseUrl, Call<T> call) {
        this.call = call;
        this.baseUrl = baseUrl;
    }

    @Override
    public void enqueue(@NonNull Callback<T> callback) {
        call.enqueue(
                new Callback<T>() {
                    @Override
                    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                        WinAnalytics.getInstance()
                                .initEventArguments(
                                        baseUrl, call.request().url().toString(), response, true);
                        if (response.isSuccessful()) {
                            WinAnalytics.getInstance()
                                    .logSuccess(baseUrl, call.request().url().toString());
                        } else {
                            WinAnalytics.getInstance()
                                    .logFailure(baseUrl, call.request().url().toString());
                        }

                        callback.onResponse(call, response);
                    }

                    @Override
                    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                        WinAnalytics.getInstance()
                                .initEventArguments(
                                        baseUrl, call.request().url().toString(), null, true);
                        WinAnalytics.getInstance()
                                .logFailure(baseUrl, call.request().url().toString());
                        callback.onFailure(call, t);
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
}
