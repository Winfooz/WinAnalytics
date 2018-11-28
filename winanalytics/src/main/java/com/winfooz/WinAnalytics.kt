package com.winfooz

import android.app.Activity
import android.app.Dialog
import android.support.annotation.CheckResult
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.annotation.UiThread
import android.util.Log
import android.view.View
import java.lang.reflect.Constructor
import java.util.*
import java.util.concurrent.Executors

/**
 * Project: MyApplication6 Created: November 15, 2018
 *
 * @author Mohamed Hamdan
 */
class WinAnalytics private constructor(private val configuration: WinConfiguration) {

    private val executor = Executors.newSingleThreadExecutor()
    private val registeredObjects = ArrayList<CallArgumentField>()
    private var indexObject: Any? = null

    fun register(any: Any) {
        executor.execute {
            any.javaClass.declaredFields.forEach {
                it.isAccessible = true
                val argument = it.getAnnotation(CallArgument::class.java)
                if (argument != null) {
                    val argumentField = CallArgumentField()
                    argumentField.className = any.javaClass.name
                    argumentField.endpoints = argument.value.toMutableList()
                    argumentField.names = argument.names.toMutableList()
                    argumentField.field = it
                    argumentField.enclosingObject = any
                    registeredObjects.add(argumentField)
                }
            }
        }
    }

    fun unregister(any: Any) {
        executor.execute {
            val iterator = registeredObjects.iterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (next.className == any.javaClass.name) {
                    iterator.remove()
                }
            }
        }
    }

    fun initEventArguments(baseUrl: String, url: String, obj: Any?, success: Boolean) {
        httpLoggingInitArguments(getExactUrl(baseUrl, url), obj, success)
    }

    protected fun logSuccess(baseUrl: String, url: String) {
        httpLogging(getExactUrl(baseUrl, url), true)
    }

    protected fun logFailure(baseUrl: String, url: String) {
        httpLogging(getExactUrl(baseUrl, url), false)
    }

    fun getConfiguration(): WinConfiguration {
        return instance.configuration
    }

    @Suppress("UNCHECKED_CAST")
    private fun httpLogging(url: String, success: Boolean) {
        executor.execute {
            val tag = if (success) "$url:success" else "$url:failure"
            if (getConfiguration().indexingClass != null) {
                val clsName = getConfiguration().indexingClass?.name
                try {
                    val classLoader = getConfiguration().indexingClass?.classLoader
                    if (classLoader != null && indexObject == null) {
                        instance.indexObject = classLoader
                            .loadClass(clsName + "_Impl")
                            .getDeclaredConstructor()
                            .newInstance()
                    }
                    if (instance.indexObject != null) {
                        val map = instance
                            .indexObject
                            ?.javaClass
                            ?.getDeclaredMethod("getEvents")
                            ?.invoke(instance.indexObject) as? Map<String, HttpEvent>
                        logEvent(map?.get(tag))
                    }
                } catch (e: Exception) {
                    throw RuntimeException("Unable to find index class for $clsName", e)
                }
            }
        }
    }

    private fun httpLoggingInitArguments(url: String, obj: Any?, success: Boolean) {
        executor.execute {
            val tag = if (success) "$url:success" else "$url:failure"
            if (getConfiguration().indexingClass != null) {
                val clsName = getConfiguration().indexingClass?.name
                try {
                    val classLoader = getConfiguration().indexingClass?.classLoader
                    if (classLoader != null && indexObject == null) {
                        instance.indexObject = classLoader
                            .loadClass(clsName + "_Impl")
                            .getDeclaredConstructor()
                            .newInstance()
                    }
                    if (instance.indexObject != null) {
                        val map = instance
                            .indexObject
                            ?.javaClass
                            ?.getDeclaredMethod("getEvents")
                            ?.invoke(instance.indexObject) as? Map<String, HttpEvent>
                        initArguments(map?.get(tag), obj)
                    }
                } catch (e: Exception) {
                    throw RuntimeException("Unable to find index class for $clsName", e)
                }
            }
        }
    }

    private fun logEvent(event: HttpEvent?) {
        if (event != null) {
            try {
                val constructor = Class.forName(event.packageName + "." + event.className)
                    .getDeclaredConstructor()
                constructor.isAccessible = true
                val instance = constructor.newInstance()
                val args = registeredObjects.filter { it.names.contains(event.name) }.map { it.field.get(it.enclosingObject) }.toTypedArray()
                instance.javaClass
                    .getDeclaredMethod(event.method, *event.parameters)
                    .invoke(instance, *args)
            } catch (ignored: Exception) {
                Log.e("", "")
            }
        }
    }

    private fun initArguments(event: HttpEvent?, obj: Any?) {
        if (event != null) {
            try {
                val enclosingObject = registeredObjects.find { it.names.contains(event.name) }?.enclosingObject
                enclosingObject?.javaClass?.declaredMethods?.forEach {
                    it.isAccessible = true
                    if (it.getAnnotation(BindCallArguments::class.java) != null) {
                        it.invoke(enclosingObject, obj)
                        return@forEach
                    }
                }
            } catch (ignored: Exception) {
                Log.e("WinAnalytics", ignored.message, ignored)
            }
        }
    }

    private fun getExactUrl(baseUrl: String, url: String): String {
        return url.split("\\?")[0].replace(baseUrl, "")
    }

    companion object {

        private val ANALYTICS: MutableMap<Class<*>?, Constructor<out Destroyable>?> = LinkedHashMap()

        @JvmStatic
        private lateinit var instance: WinAnalytics

        @JvmStatic
        fun getInstance(): WinAnalytics {
            // This implementation for known issue on the official kotlin issue tracker https://youtrack.jetbrains.com/issue/KT-21862
            try {
                return instance
            } catch (e: UninitializedPropertyAccessException) {
                throw IllegalAccessException(
                    "You must call WinAnalytics.init() in your application class")
            }
        }

        @JvmStatic
        fun init(configuration: WinConfiguration) {
            // This implementation for known issue on the official kotlin issue tracker https://youtrack.jetbrains.com/issue/KT-21862
            try {
                getInstance()
                throw RuntimeException("WinAnalytics already initialized")
            } catch (e: IllegalAccessException) {
                instance = WinAnalytics(configuration)
            }
        }

        @Suppress("UNCHECKED_CAST")
        @JvmStatic
        fun <T> create(cls: Class<T>): T {
            val clsName = cls.name
            try {
                val classLoader = cls.classLoader
                return if (classLoader != null) {
                    classLoader
                        .loadClass(clsName + "_Analytics")
                        .getDeclaredConstructor()
                        .newInstance() as T
                } else {
                    throw RuntimeException("Unable to find analytics wrapper class for $clsName")
                }
            } catch (e: Exception) {
                throw RuntimeException("Unable to find analytics wrapper class for $clsName", e)
            }
        }

        @JvmStatic
        @NonNull
        @UiThread
        fun bind(target: Activity): Destroyable {
            return bind(target, target.window.decorView)
        }

        @JvmStatic
        @NonNull
        @UiThread
        fun bind(@NonNull target: View): Destroyable {
            return bind(target, target)
        }

        @JvmStatic
        @NonNull
        @UiThread
        fun bind(@NonNull target: Dialog): Destroyable {
            return bind(target, target.window?.decorView)
        }

        @JvmStatic
        @NonNull
        @UiThread
        fun bind(@NonNull target: Any, @NonNull source: Activity): Destroyable {
            return bind(target, source.window.decorView)
        }

        @JvmStatic
        @NonNull
        @UiThread
        fun bind(@NonNull target: Any, @NonNull source: Dialog): Destroyable {
            return bind(target, source.window?.decorView)
        }

        @JvmStatic
        @NonNull
        @UiThread
        fun bind(@NonNull target: Any, @NonNull source: View?): Destroyable {
            val constructor = findBindingConstructorForClass(target.javaClass)
                ?: return Destroyable.EMPTY_DESTROYABLE
            return try {
                constructor.newInstance(target, source)
            } catch (e: Exception) {
                Destroyable.EMPTY_DESTROYABLE
            }
        }

        @Suppress("UNCHECKED_CAST")
        @Nullable
        @CheckResult
        @UiThread
        private fun findBindingConstructorForClass(cls: Class<*>?): Constructor<out Destroyable>? {
            var bindingCtor: Constructor<out Destroyable>? = ANALYTICS[cls]
            if (bindingCtor != null || ANALYTICS.containsKey(cls)) {
                return bindingCtor
            }
            val clsName = cls?.name
            bindingCtor = try {
                val bindingClass = cls?.classLoader?.loadClass(clsName + "_Analytics")
                bindingClass?.getConstructor(cls, View::class.java) as Constructor<out Destroyable>
            } catch (e: ClassNotFoundException) {
                findBindingConstructorForClass(cls?.superclass)
            }
            ANALYTICS[cls] = bindingCtor
            return bindingCtor
        }
    }
}
