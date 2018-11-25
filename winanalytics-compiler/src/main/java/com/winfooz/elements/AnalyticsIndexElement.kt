package com.winfooz.elements

import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.PackageElement
import javax.tools.Diagnostic

/**
 * Project: WinAnalytics2
 * Created: November 16, 2018
 *
 * @author Mohamed Hamdan
 */
class AnalyticsIndexElement(
    private val messager: Messager,
    private val element: Element
) {

    var className = element.simpleName.toString()
    var pkgName: String = (element.enclosingElement as PackageElement).qualifiedName.toString()

    init {
        validate()
    }

    private fun validate() {
        if (element.kind != ElementKind.INTERFACE) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Must be interface", element)
        }
    }
}