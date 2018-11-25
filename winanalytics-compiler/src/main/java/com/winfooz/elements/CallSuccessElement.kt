package com.winfooz.elements

import com.winfooz.CallSuccess
import com.winfooz.Event
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.PackageElement
import javax.tools.Diagnostic

/**
 * Project: WinAnalytics2
 * Created: November 16, 2018
 *
 * @author Mohamed Hamdan
 */
class CallSuccessElement(
    val call: CallSuccess,
    private val messager: Messager,
    val element: ExecutableElement,
    val parameters: MutableList<out Element>
) {

    var methodName: String = element.simpleName.toString()
    var pkgName: String = (element.enclosingElement.enclosingElement as PackageElement).qualifiedName.toString()
    var className: String = (element.enclosingElement.simpleName).toString()
    var name: String = call.name

    init {
        validate()
    }

    private fun validate() {
        when {
            element.getAnnotation(Event::class.java) == null -> {
                messager.printMessage(Diagnostic.Kind.ERROR, "${element.simpleName} should be annotated with @Event annotation", element)
            }
        }
    }
}