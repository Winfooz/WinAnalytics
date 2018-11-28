package com.winfooz;

import android.support.annotation.NonNull;
import java.lang.reflect.Type;
import retrofit2.Call;
import retrofit2.CallAdapter;

public class HttpCallAdapter<R> implements CallAdapter<R, Call<R>> {

    private final Type type;
    private final String baseUrl;

    HttpCallAdapter(Type type, String baseUrl) {
        this.type = type;
        this.baseUrl = baseUrl;
    }

    @NonNull
    @Override
    public Type responseType() {
        return type;
    }

    @NonNull
    @Override
    public Call<R> adapt(@NonNull Call<R> call) {
        return new HttpEnqueueAdapter<>(baseUrl, call);
    }
}
