package com.winfooz

/**
 * Project: WinAnalytics2 Created: November 23, 2018
 *
 * @author Mohamed Hamdan
 */
class HttpEvent(
    var packageName: String?,
    var className: String?,
    var method: String?,
    var name: String?,
    vararg parameters: Class<*>
) {

    var parameters: Array<out Class<*>>? = null

    init {
        this.parameters = parameters
    }
}
