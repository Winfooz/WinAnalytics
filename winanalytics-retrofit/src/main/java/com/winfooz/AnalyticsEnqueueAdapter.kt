package com.winfooz

import android.os.Handler
import android.os.Looper
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AnalyticsEnqueueAdapter<T>(
    private val baseUrl: String,
    private val call: Call<T>,
    private val logEvent: Boolean
) : Call<T> {

    private val handler: Handler by lazy { Handler(Looper.getMainLooper()) }

    override fun enqueue(callback: Callback<T>) {
        call.enqueue(
            object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (logEvent) {
                        WinAnalytics.getInstance()
                            .initEventArguments(
                                baseUrl,
                                call.request().url().toString(),
                                response,
                                true
                            ) { response(callback, response) }
                    } else {
                        response(callback, response)
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    if (logEvent) {
                        WinAnalytics.getInstance()
                            .initEventArguments(
                                baseUrl,
                                call.request().url().toString(),
                                t,
                                false
                            ) { failure(callback, t) }
                    } else {
                        failure(callback, t)
                    }
                }
            })
    }

    @Throws(IOException::class)
    override fun execute(): Response<T> {
        return call.execute()
    }

    override fun isExecuted(): Boolean {
        return call.isExecuted
    }

    override fun cancel() {
        call.cancel()
    }

    override fun isCanceled(): Boolean {
        return call.isCanceled
    }

    override fun clone(): Call<T> {
        return call.clone()
    }

    override fun request(): Request {
        return call.request()
    }

    private fun response(callback: Callback<T>, response: Response<T>) {
        if (logEvent) {
            if (response.isSuccessful) {
                WinAnalytics.getInstance().logSuccess(baseUrl, call.request().url().toString())
            } else {
                WinAnalytics.getInstance().logFailure(baseUrl, call.request().url().toString())
            }
        }
        handler.post { callback.onResponse(call, response) }
    }

    private fun failure(callback: Callback<T>, t: Throwable) {
        if (logEvent) {
            WinAnalytics.getInstance().logFailure(baseUrl, call.request().url().toString())
        }
        handler.post { callback.onFailure(call, t) }
    }
}
