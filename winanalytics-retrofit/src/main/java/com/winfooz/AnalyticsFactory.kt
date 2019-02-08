package com.winfooz

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class AnalyticsFactory(private val baseUrl: String) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (CallAdapter.Factory.getRawType(returnType) != Call::class.java) {
            return null
        }
        if (returnType !is ParameterizedType) {
            val msg = "Call must have generic type (e.g., Call<ResponseBody>)"
            throw IllegalStateException(msg)
        }
        val responseType = CallAdapter.Factory.getParameterUpperBound(0, returnType)
        return AnalyticsCallAdapter<Any>(
            responseType,
            baseUrl,
            annotations.any { it.annotationClass == AnalyticsCall::class }
        )
    }
}
