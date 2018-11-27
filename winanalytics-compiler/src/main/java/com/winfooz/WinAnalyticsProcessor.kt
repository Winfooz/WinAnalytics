package com.winfooz

import com.winfooz.elements.AnalyticsElement
import com.winfooz.elements.AnalyticsIndexElement
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

    private var javaAnalyticsWrapperElements = mutableSetOf<AnalyticsWrapperElement>()
    private var kotlinAnalyticsWrapperElements = mutableSetOf<AnalyticsWrapperElement>()

    private var javaScreenElements = mutableSetOf<ScreenElement>()
    private var kotlinScreenElements = mutableSetOf<ScreenElement>()

    private var javaAnalyticsElements = mutableSetOf<AnalyticsElement>()
    private var kotlinAnalyticsElements = mutableSetOf<AnalyticsElement>()

    private var javaEventWithClickElements = mutableSetOf<EventWithClickElement>()
    private var kotlinEventWithClickElements = mutableSetOf<EventWithClickElement>()

    private var javaAnalyticsIndexElements = mutableSetOf<AnalyticsIndexElement>()
    private var kotlinAnalyticsIndexElements = mutableSetOf<AnalyticsIndexElement>()

    private var types: Types by Delegates.notNull()
    private var elementUtils: Elements by Delegates.notNull()
    private var filer: Filer by Delegates.notNull()
    private var messager: Messager by Delegates.notNull()

    private val kotlinProcessor: Processor by lazy {
        KotlinProcessor(
            processingEnv,
            kotlinAnalyticsWrapperElements,
            kotlinScreenElements,
            kotlinAnalyticsElements,
            kotlinEventWithClickElements
        )
    }
    private val javaProcessor: Processor by lazy {
        JavaProcessor(
            filer,
            javaAnalyticsWrapperElements,
            javaScreenElements,
            javaAnalyticsElements,
            javaEventWithClickElements,
            javaAnalyticsIndexElements
        )
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        filer = processingEnv.filer
        messager = processingEnv.messager
        elementUtils = processingEnv.elementUtils
        types = processingEnv.typeUtils
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
        roundEnv.getElementsAnnotatedWith(AnalyticsWrapper::class.java)
            .forEach {
                try {
                    val kotlinMetadata = it.getAnnotation(KOTLIN_META_DATA_CLASS)
                    if (kotlinMetadata != null) {
                        kotlinAnalyticsWrapperElements.add(AnalyticsWrapperElement(messager, elementUtils, it))
                    } else {
                        javaAnalyticsWrapperElements.add(AnalyticsWrapperElement(messager, elementUtils, it))
                    }
                } catch (e: Exception) {
                    messager.printMessage(Diagnostic.Kind.ERROR, e.message, it)
                }
            }
        roundEnv.getElementsAnnotatedWith(Analytics::class.java)
            .forEach {
                try {
                    val kotlinMetadata = it.getAnnotation(KOTLIN_META_DATA_CLASS)
                    if (kotlinMetadata != null) {
                        kotlinAnalyticsElements.add(AnalyticsElement(it.getAnnotation(Analytics::class.java), messager, elementUtils, it))
                    } else {
                        javaAnalyticsElements.add(AnalyticsElement(it.getAnnotation(Analytics::class.java), messager, elementUtils, it))
                    }
                } catch (e: Exception) {
                    messager.printMessage(Diagnostic.Kind.ERROR, e.message, it)
                }
            }

        roundEnv.getElementsAnnotatedWith(EventOnClick::class.java)
            .forEach {
                try {
                    val kotlinMetadata = it.enclosingElement.getAnnotation(KOTLIN_META_DATA_CLASS)
                    if (kotlinMetadata != null) {
                        kotlinEventWithClickElements.add(EventWithClickElement(it.getAnnotation(EventOnClick::class.java), messager, elementUtils, javaAnalyticsElements.union(kotlinAnalyticsElements), it))
                    } else {
                        javaEventWithClickElements.add(EventWithClickElement(it.getAnnotation(EventOnClick::class.java), messager, elementUtils, javaAnalyticsElements.union(kotlinAnalyticsElements), it))
                    }
                } catch (e: Exception) {
                    messager.printMessage(Diagnostic.Kind.ERROR, e.message, it)
                }
            }

        roundEnv.getElementsAnnotatedWith(Screen::class.java)
            .forEach {
                try {
                    val kotlinMetadata = it.getAnnotation(KOTLIN_META_DATA_CLASS)
                    if (kotlinMetadata != null) {
                        kotlinScreenElements.add(ScreenElement(messager, it))
                    } else {
                        javaScreenElements.add(ScreenElement(messager, it))
                    }
                } catch (e: Exception) {
                    messager.printMessage(Diagnostic.Kind.ERROR, e.message, it)
                }
            }

        roundEnv.getElementsAnnotatedWith(AnalyticsIndex::class.java)
            .forEach {
                try {
                    val kotlinMetadata = it.getAnnotation(KOTLIN_META_DATA_CLASS)
                    if (kotlinMetadata != null) {
                        kotlinAnalyticsIndexElements.add(AnalyticsIndexElement(messager, it))
                    } else {
                        javaAnalyticsIndexElements.add(AnalyticsIndexElement(messager, it))
                    }
                } catch (e: Exception) {
                    messager.printMessage(Diagnostic.Kind.ERROR, e.message, it)
                }
            }
        kotlinProcessor.process()
        javaProcessor.process()
        return true
    }
}
