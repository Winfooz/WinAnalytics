package com.winfooz.winanalytics.compiler

import com.winfooz.winanalytics.compiler.models.FieldData
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

import com.winfooz.winanalytics.annotations.Analytics
import com.winfooz.winanalytics.annotations.AnalyticsEmbedded
import com.winfooz.winanalytics.annotations.AnalyticsConfiguration

object ElementUtils {

    /**
     * @param processingEnv for use processing utils like messager for log errors or warnings
     *                      or use filer to create kotlin or java files etc...
     * @param element The element that annotated with [Analytics] or [AnalyticsEmbedded]
     * @param type [AnalyticsEmbedded] or [Analytics]
     *
     * @return [FieldData] that contains the annotation values and other information about element.
     */
    fun parseAnalyticsAnnotationData(processingEnv: ProcessingEnvironment, element: Element, type: Annotation): FieldData {
        val enclosingElement = element.enclosingElement as TypeElement
        val pkg = processingEnv.elementUtils.getPackageOf(element).toString()
        return FieldData(
                element,
                pkg,
                enclosingElement.qualifiedName.toString().substring(pkg.length + 1),
                element.simpleName.toString(),
                type,
                (type as Analytics).value
        )
    }

    /**
     * @param processingEnv for use processing utils like messager for log errors or warnings
     *                      or use filer to create kotlin or java files etc...
     * @param element The element that annotated with [Analytics] or [AnalyticsEmbedded]
     * @param type [AnalyticsEmbedded] or [Analytics]
     *
     * @return [FieldData] that contains the annotation values and other information about element.
     */
    fun parseAnalyticsEmbeddedAnnotationData(processingEnv: ProcessingEnvironment, element: Element, type: Annotation): FieldData {
        val enclosingElement = element.enclosingElement as TypeElement
        val pkg = processingEnv.elementUtils.getPackageOf(element).toString()
        return FieldData(
                element,
                pkg,
                enclosingElement.qualifiedName.toString().substring(pkg.length + 1),
                element.simpleName.toString(),
                type,
                ""
        )
    }

    /**
     * @param processingEnv for use processing utils like messager for log errors or warnings
     *                      or use filer to create kotlin or java files etc...
     * @param element The element that annotated with [AnalyticsConfiguration]
     * @param type [AnalyticsConfiguration]
     *
     * @return [FieldData] that contains the annotation values and other information about element.
     */
    fun parseConfigurationData(processingEnv: ProcessingEnvironment, element: Element, type: Annotation): FieldData {
        val pkg = processingEnv.elementUtils.getPackageOf(element).toString()
        return FieldData(
                element,
                pkg,
                (element as TypeElement).qualifiedName.toString().substring(pkg.length + 1),
                element.simpleName.toString(),
                type
        )
    }
}