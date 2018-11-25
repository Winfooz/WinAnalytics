package com.winfooz.elements

import com.squareup.kotlinpoet.KModifier
import com.winfooz.Bind
import com.winfooz.EventOnClick
import com.winfooz.Name
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
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
class EventWithClickElement(
    val eventOnClick: EventOnClick,
    private val messager: Messager,
    private val elementUtils: Elements,
    private val analyticsElements: Set<AnalyticsElement>,
    private val element: Element
) {

    private var bindsElements: MutableList<Element> = mutableListOf()
    var analyticsElement: AnalyticsElement? = null
    var analyticsEvent: EventElement? = null
    var methodName: String = ""
    var className: String = ""
    var pkgName: String = ""
    var parameters: MutableList<Element?> = mutableListOf()

    init {
        methodName = element.simpleName.toString()
        pkgName = (element.enclosingElement.enclosingElement as PackageElement).qualifiedName.toString()
        className = (element.enclosingElement as TypeElement).simpleName.toString()
        validate()
    }

    private fun validate() {
        val type = (element.enclosingElement.asType() as? DeclaredType?)?.asElement() as? TypeElement?
        val fields = ElementFilter.fieldsIn(elementUtils.getAllMembers(type))

        var found = false
        analyticsElements.forEach first@{
            it.events.forEach second@{ event ->
                val bindFields = fields.filter { field -> field.getAnnotation(Bind::class.java) != null }
                val bindAnnotations = bindFields.map { field -> field.getAnnotation(Bind::class.java) }
                val filteredAnnotations = bindAnnotations.filter { field -> field.value.contains(eventOnClick.value) }
                if (event.eventName == eventOnClick.event && filteredAnnotations.size == event.parameters.size) {
                    analyticsElement = it
                    analyticsEvent = event
                    found = true
                    return@second
                }
            }
            if (found) {
                return@first
            }
        }
        if (!found) {
            messager.printMessage(Diagnostic.Kind.ERROR, """Cannot find "${eventOnClick.event}" event, The following events were found (${analyticsElements.flatMap { it -> it.events.map { it.eventName } }.joinToString { it }})""", element)
        } else {
            validateNames()
        }
    }

    private fun validateNames() {
        analyticsEvent?.parameters?.forEach {
            if (it.getAnnotation(Name::class.java) == null) {
                messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "All fields and parameters used with @EventOnClick annotation should be annotated" +
                        " with @Name annotation (${analyticsElement?.className}." +
                        "${analyticsEvent?.methodName}) parameter" +
                        " (${((it.asType() as DeclaredType).asElement() as TypeElement).qualifiedName}" +
                        " ${it.simpleName})",
                    element
                )
                return
            }
        }
        validateBinds()
    }

    private fun validateBinds() {
        val type = (element.enclosingElement.asType() as? DeclaredType?)?.asElement() as? TypeElement?
        val fields = ElementFilter.fieldsIn(elementUtils.getAllMembers(type))
        fields.forEach { it ->
            val bind = it.getAnnotation(Bind::class.java)
            if (bind != null && bind.value.any { it == eventOnClick.value }) {
                val name = it.getAnnotation(Name::class.java)
                if (name == null) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "All fields annotated with @Bind should be named with @Name", it)
                    return
                } else if (!(it.modifiers.all { it != Modifier.PRIVATE } && it.modifiers.all { it != KModifier.PRIVATE })) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "All fields annotated with @Bind cannot be private", it)
                    return
                }
                bindsElements.add(it)
            }
        }
        analyticsEvent?.parameters?.forEach {
            val bindsNames = bindsElements.map { bind -> bind.getAnnotation(Name::class.java) }
            val paramName = it.getAnnotation(Name::class.java)
            if (bindsNames.all { name -> name.value != paramName.value }) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Cannot find $paramName in ${element.enclosingElement}, analytics method (${analyticsElement?.className}.${analyticsEvent?.methodName})", element)
                return
            }
            val bindElement = bindsElements.find { bind -> bind.getAnnotation(Name::class.java).value == it.getAnnotation(Name::class.java).value }
            val paramType = ((it.asType() as? DeclaredType?)?.asElement() as? TypeElement?)?.qualifiedName?.toString()
            val fieldType = ((bindElement?.asType() as? DeclaredType?)?.asElement() as? TypeElement?)?.qualifiedName?.toString()
            if (fieldType != paramType) {
                messager.printMessage(Diagnostic.Kind.ERROR, "Type mismatch (${bindElement?.getAnnotation(Name::class.java)} $fieldType ${bindElement?.simpleName}) with (${it.getAnnotation(Name::class.java)} $paramType ${it.simpleName})", it)
            } else {
                parameters.add(bindElement)
            }
        }
    }
}
