package com.winfooz.winanalytics.compiler

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter
import com.winfooz.winanalytics.annotations.Analytics
import com.winfooz.winanalytics.annotations.AnalyticsEmbedded

object FieldUtils {

    private const val BOOLEAN_WRAPPER = "java.lang.Boolean"
    private const val BOOLEAN = "boolean"

    /**
     * For check if class contains getter method for field that annotated with [Analytics]
     * [AnalyticsEmbedded] and print error if the class doesn't contains the getter method
     * or getter method return different type.
     *
     * This method handle member style (e.g mVariableName instead of variableName)
     */
    fun checkGetter(element: Element, processingEnv: ProcessingEnvironment) {
        val enclosingElement = element.enclosingElement as TypeElement

        val elementTypeKind = element.asType().toString()

        val elementName = element.simpleName.toString()
        val elementNameLowerCase = elementName.toLowerCase()

        val possibleMethodNames = mutableListOf<String>()
        possibleMethodNames.add("get$elementNameLowerCase")
        if (elementTypeKind == BOOLEAN_WRAPPER || elementTypeKind == BOOLEAN) {
            possibleMethodNames.add("is$elementNameLowerCase")
            possibleMethodNames.add("has$elementNameLowerCase")
            possibleMethodNames.add(elementNameLowerCase)
        }

        // Handle if variable start with m (e.g mVariableName instead of variableName)
        if (elementName.length > 1 && elementName[0] == 'm' && (elementName[1] in 'A'..'Z')) {
            possibleMethodNames.add("get" + elementNameLowerCase.substring(1))
            if (elementTypeKind == BOOLEAN_WRAPPER || elementTypeKind == BOOLEAN) {
                possibleMethodNames.add("is" + elementNameLowerCase.substring(1))
                possibleMethodNames.add("has" + elementNameLowerCase.substring(1))
                possibleMethodNames.add(elementNameLowerCase.substring(1))
            }
        }

        val elementMembers = processingEnv.elementUtils.getAllMembers(enclosingElement)
        val elementMethods = ElementFilter.methodsIn(elementMembers)
        for (methodElement in elementMethods) {
            if (methodElement.parameters.size == 0) {
                val methodNameString = methodElement.simpleName.toString()
                val methodNameLowerCase = methodNameString.toLowerCase()

                if (possibleMethodNames.contains(methodNameLowerCase)) {
                    if (methodElement.parameters.size == 0 && methodElement.returnType == element.asType()
                            && !methodElement.modifiers.contains(Modifier.PRIVATE)) {
                        if (methodElement.returnType.toString() == element.asType().toString()) {
                            return
                        }
                    }
                }
            }
        }
        MessagerUtils.getterError(processingEnv.messager, element)
    }
}