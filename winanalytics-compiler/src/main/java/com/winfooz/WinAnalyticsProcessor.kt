package com.winfooz

import com.winfooz.elements.AnalyticsElement
import com.winfooz.elements.AnalyticsWrapperElement
import com.winfooz.elements.EventWithClickElement
import com.winfooz.elements.ScreenElement
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic
import kotlin.properties.Delegates

/**
 * Project: WinAnalytics2
 * Created: November 16, 2018
 *
 * @author Mohamed Hamdan
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class WinAnalyticsProcessor : AbstractProcessor() {

    private var analyticsWrapperElements = mutableSetOf<AnalyticsWrapperElement>()
    private var screenElements = mutableSetOf<ScreenElement>()
    private var analyticsElements = mutableSetOf<AnalyticsElement>()
    private var eventWithClickElements = mutableSetOf<EventWithClickElement>()

    private var types: Types by Delegates.notNull()
    private var elementUtils: Elements by Delegates.notNull()
    private var filer: Filer by Delegates.notNull()
    private var messager: Messager by Delegates.notNull()
    private var winAnalyticsIndex: String? = null

    private val javaProcessor: Processor by lazy {
        JavaProcessor(
            filer,
            analyticsWrapperElements,
            screenElements,
            analyticsElements,
            eventWithClickElements,
            winAnalyticsIndex
        )
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        filer = processingEnv.filer
        messager = processingEnv.messager
        elementUtils = processingEnv.elementUtils
        types = processingEnv.typeUtils
        winAnalyticsIndex = processingEnv.options[WIN_ANALYTICS_INDEX]
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            AnalyticsWrapper::class.java.canonicalName,
            EventOnClick::class.java.canonicalName,
            Analytics::class.java.canonicalName,
            Screen::class.java.canonicalName
        )
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        roundEnv.getElementsAnnotatedWith(AnalyticsWrapper::class.java).forEach {
            try {
                analyticsWrapperElements.add(AnalyticsWrapperElement(messager, elementUtils, it))
            } catch (e: Exception) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.message, it)
            }
        }
        roundEnv.getElementsAnnotatedWith(Analytics::class.java).forEach {
            try {
                analyticsElements.add(AnalyticsElement(
                    it.getAnnotation(Analytics::class.java),
                    messager,
                    elementUtils,
                    it
                ))
            } catch (e: Exception) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.message, it)
            }
        }

        roundEnv.getElementsAnnotatedWith(EventOnClick::class.java).forEach {
            try {
                eventWithClickElements.add(EventWithClickElement(
                    it.getAnnotation(EventOnClick::class.java),
                    messager,
                    elementUtils,
                    analyticsElements,
                    it
                ))
            } catch (e: Exception) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.message, it)
            }
        }

        roundEnv.getElementsAnnotatedWith(Screen::class.java).forEach {
            try {
                screenElements.add(ScreenElement(messager, it))
            } catch (e: Exception) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.message, it)
            }
        }
        javaProcessor.process()
        return true
    }

    private companion object {

        private const val WIN_ANALYTICS_INDEX = "winAnalyticsIndex"
    }
}
