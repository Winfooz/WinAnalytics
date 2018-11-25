package com.winfooz.elements

import com.winfooz.Data
import javax.annotation.processing.Messager
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.VariableElement
import javax.tools.Diagnostic

/**
 * Project: WinAnalytics2
 * Created: November 20, 2018
 *
 * @author Mohamed Hamdan
 */
class DataElement(
    private val data: Data,
    private val parameters: List<VariableElement>,
    private val messager: Messager,
    private val element: ExecutableElement
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
                reference = validateReference(value)
                valid = true
            }
        }
        if (!valid) {
            messager.printMessage(Diagnostic.Kind.ERROR, "${element.simpleName} there is no ${value.substring(0, value.indexOf("."))} parameter", element)
            return
        }
    }

    private fun validateReference(reference: String): String {
        val references = reference.split(".")
        var newReference = references[0]
        kotlinReference = references[0]
        references.subList(1, references.size).forEach {
            newReference += ".get${it.substring(0, 1).toUpperCase()}${it.substring(1)}()"
            kotlinReference += "?.$it"
        }
        return newReference
    }
}