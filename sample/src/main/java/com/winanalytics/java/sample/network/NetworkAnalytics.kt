package com.winanalytics.java.sample.network

import com.winanalytics.java.sample.API_METHOD_POSTS
import com.winanalytics.java.sample.FAILED_GET_STORES_EVENT
import com.winanalytics.java.sample.SUCCESS_GET_STORES_EVENT
import com.winanalytics.java.sample.models.Store
import com.winfooz.Analytics
import com.winfooz.CallFailure
import com.winfooz.CallSuccess
import com.winfooz.Data
import com.winfooz.Event
import com.winfooz.Key
import com.winfooz.Value

/**
 * Project: WinAnalytics2
 * Created: February 08, 2019
 *
 * @author Mohamed Hamdan
 */
@Analytics
interface NetworkAnalytics {

    @CallFailure(value = API_METHOD_POSTS, name = FAILED_GET_STORES_EVENT)
    @Event(
        value = FAILED_GET_STORES_EVENT,
        events = [
            Data(value = Value("throwable.message"), key = Key("message"))
        ],
        timestamp = true
    )
    fun failedGetPosts(throwable: Throwable)

    @CallSuccess(value = API_METHOD_POSTS, name = SUCCESS_GET_STORES_EVENT)
    @Event(
        value = SUCCESS_GET_STORES_EVENT,
        events = [
            Data(value = Value("store.name"), key = Key("name"))
        ],
        timestamp = true
    )
    fun successGetPosts(store: Store)
}