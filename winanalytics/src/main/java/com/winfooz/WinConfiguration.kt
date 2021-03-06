package com.winfooz

/**
 * Project: WinAnalytics2
 * Created: November 16, 2018
 *
 * @author Mohamed Hamdan
 */
class WinConfiguration {

    var adapters: MutableList<AnalyticsAdapter> = mutableListOf()
        private set
    var indexingClass: Class<*>? = null
        private set
    var debug: Boolean = false
        private set

    companion object {

        @JvmStatic
        fun builder(): Builder {
            return Builder()
        }
    }

    class Builder {

        private val adapters: MutableList<AnalyticsAdapter> by lazy { mutableListOf<AnalyticsAdapter>() }
        private var indexingClass: Class<*>? = null
        private var debugMode: Boolean = false

        fun registerAdapter(adapter: AnalyticsAdapter): Builder {
            adapters.add(adapter)
            return this
        }

        fun indexingClass(indexingClass: Class<*>): Builder {
            this.indexingClass = indexingClass
            return this
        }

        fun debugMode(debugMode: Boolean): Builder {
            this.debugMode = debugMode
            return this
        }

        fun build(): WinConfiguration {
            val winConfiguration = WinConfiguration()
            winConfiguration.adapters = adapters
            winConfiguration.indexingClass = indexingClass
            winConfiguration.debug = debugMode
            return winConfiguration
        }
    }
}
