package com.winanalytics.java.sample.test;

import com.winanalytics.java.sample.models.Post;
import com.winfooz.Analytics;
import com.winfooz.CallSuccess;
import com.winfooz.Data;
import com.winfooz.Event;
import com.winfooz.Key;
import com.winfooz.Name;
import com.winfooz.Value;

/**
 * Project: WinAnalytics2
 * Created: November 22, 2018
 *
 * @author Mohamed Hamdan
 */
@Analytics
public interface JavaMainActivityAnalytics {

    @CallSuccess(value = "posts", name = "getPostsSuccess")
    @Event(
            value = "Failed get posts",
            events = {
                    @Data(value = @Value("post.title"), key = @Key("title"))
            },
            timestamp = true
    )
    void failedGetPosts(@Name("post") Post post);
}
