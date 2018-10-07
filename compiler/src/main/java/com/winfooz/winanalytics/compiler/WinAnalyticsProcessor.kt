package com.winfooz.winanalytics.compiler

import com.winfooz.winanalytics.annotations.AnalyticsConfiguration
import com.winfooz.winanalytics.annotations.AnalyticsEmbedded
import com.winfooz.winanalytics.annotations.Analytics
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions(KAPT_KOTLIN_GENERATED_OPTION_NAME)
class WinAnalyticsProcessor : AbstractProcessor() {

    private var elementsUtil: Elements? = null

    companion object {

        private val SUPPORT_ANNOTATIONS = mutableSetOf<String>(
                Analytics::class.java.canonicalName,
                AnalyticsEmbedded::class.java.canonicalName,
                AnalyticsConfiguration::class.java.canonicalName
        )
    }

    /**
     * @see Processor.init
     */
    @Synchronized
    override fun init(env: ProcessingEnvironment) {
        super.init(env)
        elementsUtil = processingEnv.elementUtils
    }

    /**
     * @see Processor.process
     */
    override fun process(set: Set<TypeElement>, env: RoundEnvironment): Boolean {
        CodeGenerationUtils().start(processingEnv, env)
        return false
    }

    /**
     * @see Processor.getSupportedAnnotationTypes
     */
    override fun getSupportedAnnotationTypes(): Set<String> {
        return SUPPORT_ANNOTATIONS
    }
}
