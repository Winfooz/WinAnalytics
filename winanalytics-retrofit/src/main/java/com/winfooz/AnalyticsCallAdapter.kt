package com.winfooz

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class AnalyticsCallAdapter<R>(
    private val type: Type,
    private val baseUrl: String,
    private val logEvent: Boolean
) : CallAdapter<R, Call<R>> {

    override fun responseType(): Type {
        return type
    }

    override fun adapt(call: Call<R>): Call<R> {
        return AnalyticsEnqueueAdapter(baseUrl, call, logEvent)
    }
}
