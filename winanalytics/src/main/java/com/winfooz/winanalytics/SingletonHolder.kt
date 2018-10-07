package com.winfooz.winanalytics

open class SingletonHolder<out T, in A>(private val creator: (A) -> T) {

    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        if (instance != null) {
            return instance!!
        }

        return synchronized(this) {
            if (instance != null) {
                instance!!
            } else {
                val created = creator(arg)
                instance = created
                created
            }
        }
    }
}
