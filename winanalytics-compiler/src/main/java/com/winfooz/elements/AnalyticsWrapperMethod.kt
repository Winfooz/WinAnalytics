package com.winfooz.elements

import com.winfooz.Analytics
import javax.annotation.processing.Messager
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.tools.Diagnostic

/**
 * Project: WinAnalytics2
 * Created: November 16, 2018
 *
 * @author Mohamed Hamdan
 */
class AnalyticsWrapperMethod(
    private val messager: Messager,
    val element: ExecutableElement
) {

    init {
        validate()
    }

    private fun validate() {
        if (!isAnalyticsClass()) {
            messager.printMessage(Diagnostic.Kind.ERROR, "${element.simpleName} should returns type annotated with @Analytics annotation", element)
        }
    }

    private fun isAnalyticsClass(): Boolean {
        val element = ((this.element.returnType as? DeclaredType?)?.asElement() as? TypeElement?)
        if (element?.getAnnotation(Analytics::class.java) != null) {
            return true
        }
        return false
    }
}
