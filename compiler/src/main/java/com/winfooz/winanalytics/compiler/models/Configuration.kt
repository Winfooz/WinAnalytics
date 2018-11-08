package com.winfooz.winanalytics.compiler.models

import com.winfooz.winanalytics.annotations.AnalyticsConfiguration

/**
 * This class contains the configuration for enable or disable any of supported client.
 *
 * @see AnalyticsConfiguration
 */
data class Configuration(var className: String = "", var firebaseEnabled: Boolean = false, var fabricEnabled: Boolean = false,
                         var mixPanelEnabled: Boolean = false, var mixpanelKey: String = "") {

    /**
     * For check if any client supported
     */
    fun any(): Boolean = firebaseEnabled || fabricEnabled || mixPanelEnabled

}