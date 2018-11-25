package com.winfooz.elements

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
class AnalyticsWrapperElement(
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
    val methods: MutableSet<AnalyticsWrapperMethod> by lazy { mutableSetOf<AnalyticsWrapperMethod>() }

    init {
        validate()
        parseEvents()
    }

    private fun parseEvents() {
        typeElement?.let {
            elementMethods.forEach { method ->
                if (method.enclosingElement.toString() != "java.lang.Object") {
                    methods.add(AnalyticsWrapperMethod(messager, method))
                }
            }
        }
    }

    private fun validate() {
        if (element.kind != ElementKind.INTERFACE) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Must be interface", element)
        }
    }
}