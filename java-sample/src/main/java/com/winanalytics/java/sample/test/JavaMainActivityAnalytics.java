package com.winanalytics.java.sample.test;

import android.support.annotation.Keep;

import com.winanalytics.java.sample.models.Post;
import com.winfooz.Analytics;
import com.winfooz.CallFailure;
import com.winfooz.CallSuccess;
import com.winfooz.Data;
import com.winfooz.Event;
import com.winfooz.Key;
import com.winfooz.Value;

/**
 * Project: WinAnalytics2
 * Created: November 22, 2018
 *
 * @author Mohamed Hamdan
 */
@Keep
@Analytics
public interface JavaMainActivityAnalytics {

    @Keep
    @CallSuccess(value = "posts", name = "requestSuccess")
    @Event(
            value = "Success get posts",
            timestamp = true,
            events = {
                    @Data(value = @Value("post.title"), key = @Key("title"))
            }
    )
    void successGetPosts(Post post);

    @Keep
    @CallFailure(value = "posts", name = "requestFailed")
    @Event(
            value = "Failed get posts",
            timestamp = true,
            events = {
                    @Data(value = @Value("post.title"), key = @Key("title")),
                    @Data(value = @Value("post.body"), key = @Key("body"))
            }
    )
    void failedGetPosts(Post post);
}
