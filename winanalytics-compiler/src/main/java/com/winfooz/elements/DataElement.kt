package com.winfooz.elements

import com.winfooz.Data
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.tools.Diagnostic

/**
 * Project: WinAnalytics2
 * Created: November 20, 2018
 *
 * @author Mohamed Hamdan
 */
data class DataElement(
    val data: Data,
    val messager: Messager,
    val element: Element,
    val parameters: List<VariableElement> = mutableListOf()
) {

    var name: String = ""
    var reference: String = ""
    var kotlinReference: String = ""

    init {
        validate()
    }

    private fun validate() {
        if (data.key.value.isEmpty()) {
            messager.printMessage(Diagnostic.Kind.ERROR, "${element.simpleName} keys cannot be empty", element)
        } else if (data.value.value.isEmpty()) {
            messager.printMessage(Diagnostic.Kind.ERROR, "${element.simpleName} values cannot be empty", element)
        }
        name = data.key.value
        validateValue(data.value.value)
    }

    private fun validateValue(value: String) {
        if (element !is TypeElement) {
            if (!value.contains(".")) {
                if (!parameters.map { it.simpleName.toString() }.contains(value)) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Cannot find $value parameter in ${element.enclosingElement.simpleName}.${element.simpleName}", element)
                }
                reference = value
                kotlinReference = value
                return
            }
            var valid = false
            parameters.forEach {
                if (value.split(".")[0] == it.simpleName.toString()) {
                    reference = getReference(value)
                    valid = true
                }
            }
            if (!valid) {
                messager.printMessage(Diagnostic.Kind.ERROR, "${element.simpleName} there is no ${value.substring(0, value.indexOf("."))} parameter", element)
                return
            }
        } else {
            reference = getReference(value)
        }
    }

    private fun getReference(reference: String): String {
        val references = reference.split(".")
        var newReference = "${references[0]} == null ? null : ${references[0]}"
        var realReference = references[0]
        kotlinReference = references[0]
        references.subList(1, references.size).forEach {
            val name = "${it.substring(0, 1).toUpperCase()}${it.substring(1)}"
            val ref = ".get$name() == null ? null : $realReference.get$name()"
            realReference += ".get$name()"
            newReference += ref
            kotlinReference += "?.$it"
        }
        return newReference
    }
}