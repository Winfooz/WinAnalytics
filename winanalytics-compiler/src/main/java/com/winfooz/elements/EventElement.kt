package com.winfooz.elements

import com.winfooz.Event
import javax.annotation.processing.Messager
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.VariableElement
import javax.tools.Diagnostic

/**
 * Project: WinAnalytics2
 * Created: November 16, 2018
 *
 * @author Mohamed Hamdan
 */
class EventElement(
    val event: Event,
    private val messager: Messager,
    val element: ExecutableElement
) {

    var methodName: String = element.simpleName.toString()
    var eventName: String = ""
    var data: MutableList<DataElement> = mutableListOf()
    val parameters: List<VariableElement> by lazy { element.parameters }

    init {
        validate()
    }

    private fun validate() {
        when {
            element.isDefault -> {
                messager.printMessage(Diagnostic.Kind.ERROR, "${element.simpleName} cannot be default", element)
            }
            element.thrownTypes?.size != 0 -> {
                messager.printMessage(Diagnostic.Kind.ERROR, "${element.simpleName} cannot throw exception", element)
            }
            event.value.isEmpty() -> {
                messager.printMessage(Diagnostic.Kind.ERROR, "${element.simpleName} event cannot be empty", element)
            }
            event.events.isNotEmpty() -> {
                event.events.forEach {
                    data.add(DataElement(it, messager, element, parameters))
                }
            }
        }
        eventName = event.value
    }
}