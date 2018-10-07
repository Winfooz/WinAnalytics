package com.winfooz.winanalytics.compiler

import com.squareup.kotlinpoet.ClassName

val FIREBASE_ANALYTICS = ClassName("com.winfooz.winanalytics", "FirebaseAnalytics")
val FABRIC_ANALYTICS = ClassName("com.winfooz.winanalytics", "FabricAnalytics")
val MIXPANEL_ANALYTICS = ClassName("com.winfooz.winanalytics", "MixPanelAnalytics")
val CONTEXT = ClassName("android.content", "Context")
val SINGLETON_HOLDER = ClassName("com.winfooz.winanalytics", "SingletonHolder")