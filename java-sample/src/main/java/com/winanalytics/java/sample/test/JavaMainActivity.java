package com.winanalytics.java.sample.test;

import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.winanalytics.java.sample.R;
import com.winanalytics.java.sample.models.Post;
import com.winanalytics.java.sample.network.HttpHelper;
import com.winfooz.CallArgument;
import com.winfooz.WinAnalytics;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Keep
public class JavaMainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "JavaMainActivity";

    @CallArgument(value = {"posts"}, names = {"requestSuccess", "requestFailed"})
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WinAnalytics.getInstance().register(this);

        findViewById(R.id.btn_login2).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WinAnalytics.getInstance().unregister(this);
    }

    @Override
    public void onClick(View v) {
        HttpHelper.getHttpClient().getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                post = response.body().get(0);
                for (Post post : response.body()) {
                    Log.e(TAG, "onResponse: " + post.getTitle());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }
}
