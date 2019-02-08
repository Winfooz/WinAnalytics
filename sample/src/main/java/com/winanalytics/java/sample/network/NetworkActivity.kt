package com.winanalytics.java.sample.network

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.winanalytics.java.sample.API_METHOD_POSTS
import com.winanalytics.java.sample.FAILED_GET_STORES_EVENT
import com.winanalytics.java.sample.R
import com.winanalytics.java.sample.SUCCESS_GET_STORES_EVENT
import com.winanalytics.java.sample.models.Store
import com.winfooz.BindCallArguments
import com.winfooz.CallArgument
import com.winfooz.WinAnalytics
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Project: WinAnalytics2
 * Created: February 08, 2019
 *
 * @author Mohamed Hamdan
 */
class NetworkActivity : AppCompatActivity() {

    @CallArgument(value = [API_METHOD_POSTS], names = [SUCCESS_GET_STORES_EVENT])
    var store: Store? = null

    @CallArgument(value = [API_METHOD_POSTS], names = [FAILED_GET_STORES_EVENT])
    var throwable: Throwable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)
        WinAnalytics.getInstance().register(this)

        HttpHelper.getHttpClient().stores.enqueue(object : Callback<MutableList<Store>> {

            override fun onFailure(call: Call<MutableList<Store>>, t: Throwable) {

            }

            override fun onResponse(call: Call<MutableList<Store>>, response: Response<MutableList<Store>>) {
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        WinAnalytics.getInstance().unregister(this)
    }

    @BindCallArguments(API_METHOD_POSTS)
    fun onFailure(t: Throwable) {
        throwable = t
    }

    @BindCallArguments(API_METHOD_POSTS)
    fun onSuccess(response: Response<MutableList<Store>>) {
        store = response.body()?.get(0)
    }
}