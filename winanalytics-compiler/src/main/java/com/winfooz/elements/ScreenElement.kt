package com.winfooz.elements

import com.winfooz.Screen
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
class ScreenElement(
    private val messager: Messager,
    private val element: Element
) {

    var screen: Screen? = null
    var className = element.simpleName.toString()
    var pkgName: String = (element.enclosingElement as PackageElement).qualifiedName.toString()

    init {
        screen = element.getAnnotation(Screen::class.java)
        validate()
    }

    private fun validate() {
        if (element.kind != ElementKind.CLASS) {
            messager.printMessage(Diagnostic.Kind.ERROR, "@Screen accepts class only", element)
        }
    }
}
