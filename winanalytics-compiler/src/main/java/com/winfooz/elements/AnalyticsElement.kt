package com.winfooz.elements

import com.winfooz.Analytics
import com.winfooz.CallFailure
import com.winfooz.CallSuccess
import com.winfooz.Event
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.util.ElementFilter
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

/**
 * Project: WinAnalytics2
 * Created: November 16, 2018
 *
 * @author Mohamed Hamdan
 */
class AnalyticsElement(
    val analytics: Analytics,
    private val messager: Messager,
    private val elementUtils: Elements,
    private val element: Element
) {

    private val typeElement: TypeElement? by lazy {
        (element.asType() as? DeclaredType?)?.asElement() as? TypeElement?
    }
    private val elementMethods: MutableList<ExecutableElement>by lazy {
        ElementFilter.methodsIn(elementUtils.getAllMembers(typeElement))
    }
    var className = element.simpleName.toString()
    var pkgName: String = (element.enclosingElement as PackageElement).qualifiedName.toString()
    val events: MutableSet<EventElement> by lazy { mutableSetOf<EventElement>() }
    val successes: MutableSet<CallSuccessElement> by lazy { mutableSetOf<CallSuccessElement>() }
    val failures: MutableSet<CallFailureElement> by lazy { mutableSetOf<CallFailureElement>() }
    var data: MutableList<DataElement> = mutableListOf()

    init {
        validate()
        parseEvents()
        parseDataAnnotations()
    }

    private fun parseEvents() {
        typeElement?.let {
            elementMethods.forEach { method ->
                val event = method.getAnnotation(Event::class.java)
                if (event != null) {
                    events.add(EventElement(event, messager, method))
                }
                val callSuccess = method.getAnnotation(CallSuccess::class.java)
                if (callSuccess != null) {
                    if (events.size > 0) {
                        val params = events.firstOrNull { e ->
                            e.methodName == method.simpleName.toString()
                        }?.parameters?.toMutableList()
                        params?.let { p ->
                            successes.add(CallSuccessElement(callSuccess, messager, method, p))
                        }
                    }
                }
                val callFailure = method.getAnnotation(CallFailure::class.java)
                if (callFailure != null) {
                    if (events.size > 0) {
                        val params = events.firstOrNull { e ->
                            e.methodName == method.simpleName.toString()
                        }?.parameters?.toMutableList()
                        params?.let { p ->
                            failures.add(CallFailureElement(callFailure, messager, method, p))
                        }
                    }
                }
            }
        }
    }

    private fun parseDataAnnotations() {
        analytics.events.forEach {
            data.add(DataElement(it, messager, element))
        }
    }

    private fun validate() {
        if (element.kind != ElementKind.INTERFACE) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Must be interface", element)
        }
    }
}