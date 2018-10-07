package com.winfooz.winanalytics.compiler

import com.winfooz.winanalytics.annotations.AnalyticsConfiguration
import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

object MessagerUtils {

    fun analyticsError(messager: Messager, element: Element, cls: Class<out Annotation>) {
        val enclosingElement = element.enclosingElement as TypeElement
        val className = enclosingElement.qualifiedName.toString()
        messager.printMessage(Diagnostic.Kind.ERROR, "@" + cls.simpleName
                + " field type must be primitive(" +
                "int, " +
                "boolean, " +
                "long, " +
                "short, " +
                "byte, " +
                "double, " +
                "float" +
                ") or String " +
                "(" + className + "." + element.simpleName.toString() + ").",
                element)
    }

    fun embeddedError(messager: Messager, element: Element, cls: Class<out Annotation>) {
        val enclosingElement = element.enclosingElement as TypeElement
        val className = enclosingElement.qualifiedName.toString()
        messager.printMessage(Diagnostic.Kind.ERROR, "@" + cls.simpleName
                + " field type mustn't be primitive(" +
                "int, " +
                "boolean, " +
                "long, " +
                "short, " +
                "byte, " +
                "double, " +
                "float" +
                ") or String " +
                "(" + className + "." + element.simpleName.toString() + ").",
                element)
    }

    fun getterError(messager: Messager, element: Element) {
        val enclosingElement = element.enclosingElement as TypeElement
        val className = enclosingElement.qualifiedName.toString()
        messager.printMessage(Diagnostic.Kind.ERROR, "@" + className
                + " Cannot find getter for field " + element.simpleName.toString(),
                element)
    }

    fun configurationError(messager: Messager) {
        messager.printMessage(Diagnostic.Kind.ERROR, "Application class must be annotated with " + AnalyticsConfiguration::class.simpleName)
    }

    fun configurationError(messager: Messager, element: Element) {
        messager.printMessage(Diagnostic.Kind.ERROR, "@"
                + AnalyticsConfiguration::class.simpleName
                + " not applicable to " + element.simpleName.toString(), element)
    }

    fun warning(messager: Messager, str: String) {
        messager.printMessage(Diagnostic.Kind.WARNING, "Warning $str")
    }
}
