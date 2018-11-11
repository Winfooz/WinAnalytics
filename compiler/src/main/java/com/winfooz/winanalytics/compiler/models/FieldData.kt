package com.winfooz.winanalytics.compiler.models

import com.winfooz.winanalytics.annotations.Event
import com.winfooz.winanalytics.annotations.AnalyticsEmbedded
import com.winfooz.winanalytics.annotations.Analytics
import javax.lang.model.element.Element

/**
 * This class contains the data for every field annotated with [Analytics] annotation.
 *
 * @see Analytics
 * @see AnalyticsEmbedded
 */
class FieldData(
        /**
         * For access class that contains current element.
         */
        var element: Element,

        /**
         * Package that contains the class field which annotated with [Analytics] annotation
         */
        var pkgName: String,

        /**
         * Class name that contains the class field which annotated with [Analytics] annotation
         */
        var className: String,

        /**
         * Class field reference that annotated with [Analytics] annotation
         */
        var reference: String,

        /**
         * The [Analytics] annotation for read [Event] annotations.
         */
        var type: Annotation,

        /**
         * For custom field name.
         */
        var eventName: String = ""
)