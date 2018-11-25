package com.winfooz

/**
 * Project: WinAnalytics2
 * Created: November 16, 2018
 *
 * @author Mohamed Hamdan
 */
fun String.toCamelCase() = "${this.substring(0, 1).toLowerCase()}${this.substring(1)}"
