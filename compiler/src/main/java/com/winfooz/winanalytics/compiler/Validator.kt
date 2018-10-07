package com.winfooz.winanalytics.compiler

import com.winfooz.winanalytics.annotations.AnalyticsConfiguration
import com.winfooz.winanalytics.annotations.AnalyticsEmbedded
import com.winfooz.winanalytics.annotations.Analytics
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind

object Validator {

    private val SUPPORTED_TYPES = setOf(
            "java.lang.String",
            "java.lang.Integer",
            "java.lang.Boolean",
            "java.lang.Long",
            "java.lang.Short",
            "java.lang.Byte",
            "java.lang.Double",
            "java.lang.Float",
            "int",
            "boolean",
            "long",
            "short",
            "byte",
            "double",
            "float"
    )

    /**
     * For validate element types and modifiers.
     */
    fun validate(element: Element, processingEnv: ProcessingEnvironment, annotation: Annotation): Boolean {
        return when (annotation) {
            is Analytics -> {
                FieldUtils.checkGetter(element, processingEnv)
                validatePrimitive(element)
            }
            is AnalyticsEmbedded -> {
                FieldUtils.checkGetter(element, processingEnv)
                validateReference(element)
            }
            is AnalyticsConfiguration -> {
                validateAnalyticsConfiguration(element)
            }
            else -> {
                false
            }
        }
    }

    /**
     * For check if element is field and primitive.
     */
    private fun validatePrimitive(element: Element): Boolean {
        return SUPPORTED_TYPES.contains(element.asType().toString()) && element.kind == ElementKind.FIELD
    }

    /**
     * For check if element is field and reference object.
     */
    private fun validateReference(element: Element): Boolean {
        return (!SUPPORTED_TYPES.contains(element.asType().toString())) && element.kind == ElementKind.FIELD
    }

    /**
     * For check the element is class.
     */
    private fun validateAnalyticsConfiguration(element: Element): Boolean {
        return element.kind == ElementKind.CLASS
    }
}
