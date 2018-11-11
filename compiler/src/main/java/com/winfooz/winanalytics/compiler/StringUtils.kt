package com.winfooz.winanalytics.compiler

/**
 * Project: WinAnalytics
 * Created: November 09, 2018
 *
 * @author Mohamed Hamdan
 */
fun String.toCamelCase(): String {
    val parts = split(" ")
    var camelCaseString = ""
    parts.forEach {
        camelCaseString += it.toProperCase()
    }
    camelCaseString = "${camelCaseString.first().toLowerCase()}${camelCaseString.substring(1)}"
    return camelCaseString
}

fun String.toProperCase(): String = "${substring(0, 1).toUpperCase()}${substring(1).toLowerCase()}"