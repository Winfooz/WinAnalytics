package com.winfooz.winanalytics.compiler

import com.winfooz.winanalytics.annotations.AnalyticsConfiguration
import com.winfooz.winanalytics.annotations.AnalyticsEmbedded
import com.winfooz.winanalytics.annotations.Analytics
import com.winfooz.winanalytics.compiler.models.FieldData
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

/**
 * For parsing and validate supported annotations.
 *
 * @see ProcessingEnvironment
 * @see RoundEnvironment
 * @see AnalyticsConfiguration
 * @see Analytics
 * @see AnalyticsEmbedded
 * @see Validator
 * @see MessagerUtils
 */
object AnnotationParsingUtils {

    /**
     * For parse supported annotations
     *
     * @param processingEnv for use processing utils like messager for log errors or warnings
     *                      or use filer to create kotlin or java files etc...
     * @param env for get elements which annotated with supported annotations
     *
     * @see AnalyticsConfiguration
     * @see Analytics
     * @see AnalyticsEmbedded
     */
    fun parseAnnotations(processingEnv: ProcessingEnvironment, env: RoundEnvironment): Set<FieldData> {
        val annotationsSet = mutableSetOf<FieldData>()
        parseAnalyticsConfiguration(processingEnv, env, annotationsSet)
        parseAnalytics(processingEnv, env, annotationsSet)
        parseAnalyticsEmbedded(processingEnv, env, annotationsSet)
        return annotationsSet
    }

    /**
     * This method for parsing [AnalyticsConfiguration] annotations and validate elements and add
     * [FieldData] with filled data to the set.
     *
     * @param annotationsSet for collect all elements with one set.
     *
     * @see Validator.validateAnalyticsConfiguration
     * @see MessagerUtils.configurationError
     * @see AnnotationParsingUtils.parseAnnotations
     * @see ElementUtils.parseConfigurationData
     */
    private fun parseAnalyticsConfiguration(processingEnv: ProcessingEnvironment, env: RoundEnvironment, annotationsSet: MutableSet<FieldData>) {
        for (element in env.getElementsAnnotatedWith(AnalyticsConfiguration::class.java)) {
            val analyticsEmbedded = element.getAnnotation(AnalyticsConfiguration::class.java)
            if (Validator.validate(element, processingEnv, analyticsEmbedded)) {
                annotationsSet.add(ElementUtils.parseConfigurationData(processingEnv, element, analyticsEmbedded))
            } else {
                MessagerUtils.configurationError(processingEnv.messager, element)
            }
        }
    }

    /**
     * This method for parsing [Analytics] annotations and validate elements and add
     * [FieldData] with filled data to the set.
     *
     * @param annotationsSet for collect all elements with one set.
     *
     * @see Validator.validatePrimitive
     * @see MessagerUtils.analyticsError
     * @see AnnotationParsingUtils.parseAnnotations
     */
    private fun parseAnalytics(processingEnv: ProcessingEnvironment, env: RoundEnvironment, annotationsSet: MutableSet<FieldData>) {
        for (element in env.getElementsAnnotatedWith(Analytics::class.java)) {
            if (element.enclosingElement is TypeElement) {
                val analytics = element.getAnnotation(Analytics::class.java)
                if (Validator.validate(element, processingEnv, analytics)) {
                    annotationsSet.add(ElementUtils.parseAnalyticsAnnotationData(processingEnv, element, analytics))
                } else {
                    MessagerUtils.analyticsError(processingEnv.messager, element, Analytics::class.java)
                }
            }
        }
    }

    /**
     * This method for parsing [AnalyticsEmbedded] annotations and validate elements and add
     * [FieldData] with filled data to the set.
     *
     * @param annotationsSet for collect all elements with one set.
     *
     * @see Validator.validateReference
     * @see MessagerUtils.embeddedError
     * @see AnnotationParsingUtils.parseAnnotations
     */
    private fun parseAnalyticsEmbedded(processingEnv: ProcessingEnvironment, env: RoundEnvironment, annotationsSet: MutableSet<FieldData>) {
        for (element in env.getElementsAnnotatedWith(AnalyticsEmbedded::class.java)) {
            if (element.enclosingElement is TypeElement) {
                val analyticsEmbedded = element.getAnnotation(AnalyticsEmbedded::class.java)
                if (Validator.validate(element, processingEnv, analyticsEmbedded)) {
                    annotationsSet.add(ElementUtils.parseAnalyticsEmbeddedAnnotationData(processingEnv, element, analyticsEmbedded))
                } else {
                    MessagerUtils.embeddedError(processingEnv.messager, element, AnalyticsEmbedded::class.java)
                }
            }
        }
    }
}
