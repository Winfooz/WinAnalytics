package com.winfooz.winanalytics.compiler

import com.winfooz.winanalytics.annotations.AnalyticsConfiguration
import com.winfooz.winanalytics.annotations.AnalyticsEmbedded
import com.winfooz.winanalytics.annotations.Analytics
import com.winfooz.winanalytics.compiler.models.Configuration
import com.winfooz.winanalytics.compiler.models.FieldData
import com.winfooz.winanalytics.compiler.types.AnalyticsType
import com.winfooz.winanalytics.compiler.types.ConfigurationType
import com.winfooz.winanalytics.compiler.types.Type
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.util.ElementFilter

class CodeGenerationUtils {

    private var configurationType: ConfigurationType? = null
    private var configuration: Configuration = Configuration()
    private var hasConfiguration: Boolean = false
    private val classesMap = LinkedHashMap<String, Any>()

    /**
     * For start parsing annotations and validate and generate code bases on annotations values.
     *
     * @param processingEnv for use processing utils like messager for log errors or warnings
     *                      or use filer to create kotlin or java files etc...
     * @param env for get elements which annotated with supported annotations
     *
     * @see AnnotationParsingUtils.parseAnnotations
     * @see Analytics
     * @see AnalyticsEmbedded
     * @see AnalyticsConfiguration
     * @see MessagerUtils
     */
    fun start(processingEnv: ProcessingEnvironment, env: RoundEnvironment) {
        val annotationsSet = AnnotationParsingUtils.parseAnnotations(processingEnv, env)
        if (annotationsSet.isNotEmpty()) {
            annotationsSet.forEach {
                when {
                    it.type is Analytics -> {
                        generateAnalyticsCode(processingEnv, it)
                    }
                    it.type is AnalyticsEmbedded -> {
                        generateAnalyticsEmbeddedCode(processingEnv, it)
                    }
                    it.type is AnalyticsConfiguration -> {
                        hasConfiguration = true
                        generateAnalyticsConfigurationCode(processingEnv, it)
                    }
                }
            }

            if (!hasConfiguration) {
                MessagerUtils.configurationError(processingEnv.messager)
            } else {
                classesMap.entries.forEach {
                    (it.value as Type).build()
                }
            }
        }
    }

    /**
     * For generate analytics classes with all events methods.
     *
     * @param processingEnv for use processing utils like messager for log errors or warnings
     *                      or use filer to create kotlin or java files etc...
     * @param data the class field for add to analytics class.
     */
    private fun generateAnalyticsCode(processingEnv: ProcessingEnvironment, data: FieldData) {
        val analyticsType: AnalyticsType?
        if (classesMap[data.className] != null) {
            analyticsType = classesMap[data.className] as? AnalyticsType?
            analyticsType?.addStatement(data)
        } else {
            analyticsType = AnalyticsType(processingEnv, data.pkgName, data.className, configuration, configurationType)
            analyticsType.addStatement(data)
            classesMap[data.className] = analyticsType
        }
    }

    /**
     * For scan classes that annotated with [AnalyticsEmbedded].
     *
     * @param processingEnv for use processing utils like messager for log errors or warnings
     *                      or use filer to create kotlin or java files etc...
     * @param data the class field for add to analytics class.
     */
    private fun generateAnalyticsEmbeddedCode(processingEnv: ProcessingEnvironment, data: FieldData) {
        var typeHelper = classesMap[data.className] as? AnalyticsType?
        if (typeHelper == null) {
            typeHelper = AnalyticsType(processingEnv, data.pkgName, data.className, configuration, configurationType)
            classesMap[data.className] = typeHelper
        }
        val elementMembers = processingEnv.elementUtils.getAllMembers((data.element.asType() as DeclaredType).asElement() as TypeElement)
        val elementMethods = ElementFilter.fieldsIn(elementMembers)
        elementMethods?.forEach {
            if (it.getAnnotation(Analytics::class.java) != null) {
                val newData = FieldData(
                        data.element,
                        data.pkgName,
                        data.className,
                        data.name + "?." + it.simpleName.toString(),
                        it.getAnnotation(Analytics::class.java)
                )
                typeHelper.addStatement(newData)
            }
        }
    }

    /**
     * For generate Analytics singleton class.
     *
     * @param processingEnv for use processing utils like messager for log errors or warnings
     *                      or use filer to create kotlin or java files etc...
     * @param data the class field for add to analytics class.
     */
    private fun generateAnalyticsConfigurationCode(processingEnv: ProcessingEnvironment, data: FieldData) {
        configurationType = ConfigurationType(processingEnv, data.pkgName, configuration)
        configurationType?.addStatement(data)
        classesMap[data.className] = configurationType!!
    }
}
