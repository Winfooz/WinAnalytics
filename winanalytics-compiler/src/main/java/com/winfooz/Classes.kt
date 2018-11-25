@file:Suppress("UNCHECKED_CAST")

package com.winfooz

import com.squareup.javapoet.ClassName

/**
 * Project: WinAnalytics2
 * Created: November 16, 2018
 *
 * @author Mohamed Hamdan
 */
var PAIR_CLASS_NAME = ClassName.get("android.util", "Pair")!!
val PAIR_KOTLIN_CLASS_NAME = com.squareup.kotlinpoet.ClassName("android.util", "Pair")
val HASH_MAP_CLASS_NAME = ClassName.get("java.util", "HashMap")!!
val HASH_MAP_KOTLIN_CLASS_NAME = com.squareup.kotlinpoet.ClassName("java.util", "HashMap")
val ANALYTICS_CLASS_NAME = ClassName.get("com.winfooz", "WinAnalytics")!!
val ANALYTICS_KOTLIN_CLASS_NAME = com.squareup.kotlinpoet.ClassName("com.winfooz", "WinAnalytics")
val ANALYTICS_ADAPTER_CLASS_NAME = ClassName.get("com.winfooz", "AnalyticsAdapter")!!
val DESTROYABLE_CLASS_NAME = ClassName.get("com.winfooz", "Destroyable")!!
val DESTROYABLE_KOTLIN_CLASS_NAME = com.squareup.kotlinpoet.ClassName("com.winfooz", "Destroyable")
val VIEW_CLASS_NAME = ClassName.get("android.view", "View")!!
val VIEW_KOTLIN_CLASS_NAME = com.squareup.kotlinpoet.ClassName("android.view", "View")
val ON_CLICK_LISTENER_CLASS_NAME = ClassName.get("android.view.View", "OnClickListener")!!
val KEEP_CLASS_NAME = ClassName.get("android.support.annotation", "Keep")!!
val KEEP_KOTLIN_CLASS_NAME = com.squareup.kotlinpoet.ClassName("android.support.annotation", "Keep")
val HTTP_EVENT_CLASS_NAME = ClassName.get("com.winfooz", "HttpEvent")!!
val STRING_CLASS_NAME = ClassName.get("java.lang", "String")!!

val KOTLIN_META_DATA_CLASS: Class<Annotation>? by lazy {
    try {
        Class.forName("kotlin.Metadata") as Class<Annotation>
    } catch (e: Throwable) {
        null
    }
}
