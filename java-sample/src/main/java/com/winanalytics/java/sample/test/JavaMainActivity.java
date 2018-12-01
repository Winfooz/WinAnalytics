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
import com.winfooz.Bind;
import com.winfooz.BindCallArguments;
import com.winfooz.CallArgument;
import com.winfooz.Name;
import com.winfooz.Screen;
import com.winfooz.WinAnalytics;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Keep
@Screen("Mohamed")
public class JavaMainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "JavaMainActivity";

    @Name("post")
    @Bind(R.id.btn_login1)
    @CallArgument(value = {"posts"}, names = {"requestSuccess", "requestFailed"})
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WinAnalytics.getInstance().register(this);
        WinAnalytics.bind(this);

        findViewById(R.id.btn_login2).setOnClickListener(this);
    }

    @BindCallArguments(value = {"posts"})
    void init(Response<List<Post>> response) {
        post = response.body().get(0);
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
