package com.winanalytics.java.sample.network;

import com.winanalytics.java.sample.models.Store;
import com.winfooz.AnalyticsCall;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

import static com.winanalytics.java.sample.ConstantsKt.API_METHOD_POSTS;

/**
 * Project: WinAnalytics2
 * Created: November 23, 2018
 *
 * @author Mohamed Hamdan
 */
public interface HttpClient {

    @AnalyticsCall
    @GET(API_METHOD_POSTS)
    Call<List<Store>> getStores();
}
