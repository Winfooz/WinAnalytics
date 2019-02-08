package com.winfooz

import java.lang.reflect.Field

/**
 * Project: WinAnalytics2 Created: November 23, 2018
 *
 * @author Mohamed Hamdan
 */
data class CallArgumentField(
    var field: Field? = null,

    var enclosingObject: Any? = null,

    var endpoints: List<String>? = null,

    var names: List<String>? = null,

    var className: String? = null
)
