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
val HASH_MAP_CLASS_NAME = ClassName.get("java.util", "HashMap")!!
val ANALYTICS_CLASS_NAME = ClassName.get("com.winfooz", "WinAnalytics")!!
val ANALYTICS_ADAPTER_CLASS_NAME = ClassName.get("com.winfooz", "AnalyticsAdapter")!!
val DESTROYABLE_CLASS_NAME = ClassName.get("com.winfooz", "Destroyable")!!
val VIEW_CLASS_NAME = ClassName.get("android.view", "View")!!
val ON_CLICK_LISTENER_CLASS_NAME = ClassName.get("android.view.View", "OnClickListener")!!
val KEEP_CLASS_NAME = ClassName.get("android.support.annotation", "Keep")!!
val HTTP_EVENT_CLASS_NAME = ClassName.get("com.winfooz", "HttpEvent")!!
val STRING_CLASS_NAME = ClassName.get("java.lang", "String")!!
