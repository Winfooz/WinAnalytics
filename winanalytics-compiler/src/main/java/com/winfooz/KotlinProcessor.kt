package com.winfooz

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import com.winfooz.elements.AnalyticsElement
import com.winfooz.elements.AnalyticsWrapperElement
import com.winfooz.elements.AnalyticsWrapperMethod
import com.winfooz.elements.EventElement
import com.winfooz.elements.EventWithClickElement
import com.winfooz.elements.ScreenElement
import org.jetbrains.annotations.Nullable
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.tools.Diagnostic

/**
 * Project: WinAnalytics2
 * Created: November 16, 2018
 *
 * @author Mohamed Hamdan
 */
class KotlinProcessor(
    private var processingEnv: ProcessingEnvironment,
    private var analyticsWrapperElements: MutableSet<AnalyticsWrapperElement>,
    private var screenElements: MutableSet<ScreenElement>,
    private var analyticsElements: MutableSet<AnalyticsElement>,
    private var eventWithClickElements: MutableSet<EventWithClickElement>
) : Processor {

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun process() {
        generateAnalytics()
        generateAnalyticsWrappers()
        generateEventWithClick()
        generateScreens()
    }

    private fun generateAnalyticsWrappers() {
        analyticsWrapperElements.forEach { element ->
            val implClass = TypeSpec.classBuilder("${element.className}_Impl")
                .addSuperinterface(ClassName(element.pkgName, element.className))
                .addModifiers(KModifier.PUBLIC)
            element.methods.forEach { it ->
                val function = generateAnalyticsWrapperFunction(implClass, it)
                function?.let {
                    implClass.addFunction(function)
                }
            }
            createFile("${element.className}_Impl", element.pkgName, implClass.build())
        }
    }

    private fun generateAnalytics() {
        analyticsElements.forEach { element ->
            val implClass = TypeSpec.classBuilder("${element.className}_Impl")
                .addSuperinterface(ClassName(element.pkgName, element.className))
                .addModifiers(KModifier.PUBLIC)
            element.events.forEach { it ->
                val method = generateAnalyticsMethod(it)
                method?.let {
                    implClass.addFunction(it)
                }
            }
            createFile("${element.className}_Impl", element.pkgName, implClass.build())
        }
    }

    private fun generateAnalyticsMethod(event: EventElement): FunSpec? {
        return try {
            val method = FunSpec.builder(event.element.simpleName.toString())
                .addModifiers(KModifier.OVERRIDE)
            event.parameters.forEach {
                if (it.getAnnotation(Nullable::class.java) != null) {
                    if (((it.asType() as DeclaredType).asElement() as TypeElement).qualifiedName.toString() == "java.lang.String") {
                        method.addParameter(ParameterSpec.builder(it.simpleName.toString(), String::class.asTypeName().asNullable()).build())
                    } else {
                        method.addParameter(it.simpleName.toString(), it.asType().asTypeName().asNullable())
                    }
                } else {
                    if (((it.asType() as DeclaredType).asElement() as TypeElement).qualifiedName.toString() == "java.lang.String") {
                        method.addParameter(ParameterSpec.builder(it.simpleName.toString(), String::class).build())
                    } else {
                        method.addParameter(it.simpleName.toString(), it.asType().asTypeName())
                    }
                }
            }
            method.addStatement(
                "val pair: %T<%T, %T<%T, %T?>> = %T(%S, %T())",
                PAIR_KOTLIN_CLASS_NAME,
                String::class,
                HASH_MAP_KOTLIN_CLASS_NAME,
                String::class,
                Any::class,
                PAIR_KOTLIN_CLASS_NAME,
                event.eventName,
                HASH_MAP_KOTLIN_CLASS_NAME
            )
            event.data.forEach {
                method.addStatement(
                    "%N.put(%S, %N)",
                    "pair.second",
                    it.name,
                    it.kotlinReference
                )
            }
            if (event.event.timestamp) {
                method.addStatement(
                    "%N.put(%S, %N)",
                    "pair.second",
                    "timestamp",
                    "System.currentTimeMillis()"
                )
            }
            method.beginControlFlow(
                "\nfor(adapter in %T.%N().%N)",
                ANALYTICS_KOTLIN_CLASS_NAME,
                "getInstance().getConfiguration",
                "adapters"
            )
            method.addStatement(
                "%N.log(%N)",
                "adapter",
                "pair"
            )
            method.endControlFlow()
            method.build()
        } catch (e: Exception) {
            null
        }
    }

    private fun generateAnalyticsWrapperFunction(implClass: TypeSpec.Builder, analyticsMethod: AnalyticsWrapperMethod): FunSpec? {
        val method = FunSpec.overriding(analyticsMethod.element)
        return try {
            val packageName = ((analyticsMethod.element.returnType as DeclaredType).asElement().enclosingElement as PackageElement).qualifiedName.toString()
            val className = (analyticsMethod.element.returnType as DeclaredType).asElement().simpleName.toString()

            implClass.addProperty(PropertySpec.builder(className.toCamelCase(), ClassName(packageName, className), KModifier.PRIVATE).delegate(CodeBlock.of("lazy { %T() }", ClassName(packageName, "${className}_Impl"))).build())
            method.addStatement("return ${className.toCamelCase()}")
            method.build()
        } catch (e: Exception) {
            null
        }
    }

    private fun generateEventWithClick() {
        val groups = eventWithClickElements.groupBy { "${it.pkgName},${it.className}" }
        groups.keys.forEach { it ->
            val className = it.split(",")[1]
            val pkgName = it.split(",")[0]
            val implClass = TypeSpec.classBuilder("${className}_Analytics")
                .addSuperinterface(DESTROYABLE_KOTLIN_CLASS_NAME)
                .addAnnotation(KEEP_KOTLIN_CLASS_NAME)
                .addModifiers(KModifier.PUBLIC)

            addClassMembers(implClass, groups[it]?.toMutableList())
            addEventWithClickConstructor(className, groups[it]?.toMutableList(), pkgName, implClass)

            overrideDestroyMethod(groups[it]?.toMutableList(), implClass)
            createFile("${className}_Analytics", pkgName, implClass.build())
        }
    }

    private fun overrideDestroyMethod(fields: MutableList<EventWithClickElement>?, implClass: TypeSpec.Builder?) {
        val function = FunSpec.builder("destroy")
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)

        function.beginControlFlow("if (target == null)")
        function.addStatement("throw %T(%S)", IllegalStateException::class, "Already destroyed")
        function.endControlFlow()
        fields?.forEach {
            function.addStatement("${it.methodName}?.setOnClickListener(null)")
            function.addStatement("${it.methodName} = null")
            function.addCode("\n")
        }
        function.addStatement("target = null")
        implClass?.addFunction(function.build())
    }

    private fun addEventWithClickConstructor(className: String, fields: MutableList<EventWithClickElement>?, pkgName: String, implClass: TypeSpec.Builder?) {
        val function = FunSpec.constructorBuilder()
            .addParameter(ParameterSpec.builder("target", ClassName(pkgName, className).asNullable()).build())
            .addStatement("this.target = target")
            .addModifiers(KModifier.PUBLIC)
        implClass?.addProperty(PropertySpec.builder("target", ClassName(pkgName, className).asNullable(), KModifier.PRIVATE).mutable(true).build())
        fields?.forEach { it ->
            it.analyticsElement?.pkgName?.let { pkgName ->
                if (implClass?.build()?.propertySpecs?.map { it.name }?.contains(it.analyticsElement?.className?.toCamelCase()) != true) {
                    implClass?.addProperty(PropertySpec
                        .builder(it.analyticsElement?.className?.toCamelCase()!!, ClassName(pkgName, it.analyticsElement?.className!!).asNullable(), KModifier.PRIVATE)
                        .delegate(CodeBlock.of("lazy { %N() }", "${it.analyticsElement?.className}_Impl"))
                        .build())
                }
                function.addStatement("%N = target?.findViewById(%L)", it.methodName, it.eventOnClick.value)
                function.addCode("%N?.setOnClickListener {\n", it.methodName)
                function.addCode("  ${it.analyticsElement?.className?.toCamelCase()}?.${it.analyticsEvent?.methodName}(${it.parameters.joinToString { param -> "target?.${param?.simpleName}" }});\n")
                function.addCode("  target?.${it.methodName}();\n")
                function.addCode("  }\n")
            }
        }
        val screenElement = screenElements.find { "${it.pkgName}.${it.className}" == "$pkgName.$className" }
        screenElements.remove(screenElement)
        generateScreenNameLog(function, screenElement)
        implClass?.addFunction(function.build())
    }

    private fun addClassMembers(implClass: TypeSpec.Builder?, fields: MutableList<EventWithClickElement>?) {
        fields?.forEach {
            implClass?.addProperty(PropertySpec.builder(it.methodName, VIEW_KOTLIN_CLASS_NAME.asNullable(), KModifier.PRIVATE).mutable(true).build())
        }
    }

    private fun generateScreenNameLog(method: FunSpec.Builder, screen: ScreenElement?) {
        if (screen != null) {
            method.addStatement(
                "val pair: %T<%T, %T<%T, %T?>> = %T(%S, %T())",
                PAIR_KOTLIN_CLASS_NAME,
                String::class,
                HASH_MAP_KOTLIN_CLASS_NAME,
                String::class,
                Any::class,
                PAIR_KOTLIN_CLASS_NAME,
                screen.screen?.value!!,
                HASH_MAP_KOTLIN_CLASS_NAME
            )
            method.addStatement(
                "%N.put(%S, %S)",
                "pair.second",
                "screen_name",
                screen.screen?.value!!
            )
            if (screen.screen?.timestamp == true) {
                method.addStatement(
                    "%N.put(%S, %N)",
                    "pair.second",
                    "timestamp",
                    "System.currentTimeMillis()"
                )
            }
            method.beginControlFlow(
                "\nfor(adapter in %T.%N().%N)",
                ANALYTICS_KOTLIN_CLASS_NAME,
                "getInstance().getConfiguration",
                "adapters"
            )
            method.addStatement(
                "%N.log(%N)",
                "adapter",
                "pair"
            )
            method.endControlFlow()
            method.build()
        }
    }

    private fun generateScreens() {
        screenElements.forEach { it ->
            val implClass = TypeSpec.classBuilder("${it.className}_Analytics")
                .addSuperinterface(DESTROYABLE_KOTLIN_CLASS_NAME)
                .addAnnotation(KEEP_KOTLIN_CLASS_NAME)
                .addModifiers(KModifier.PUBLIC)
            overrideScreenDestroyMethod(implClass)
            addScreenConstructor(it, implClass)
            createFile("${it.className}_Analytics", it.pkgName, implClass.build())
        }
    }

    private fun overrideScreenDestroyMethod(implClass: TypeSpec.Builder?) {
        val function = FunSpec.builder("destroy")
            .addModifiers(KModifier.PUBLIC, KModifier.OVERRIDE)
        implClass?.addFunction(function.build())
    }

    private fun addScreenConstructor(screen: ScreenElement, implClass: TypeSpec.Builder?) {
        val function = FunSpec.constructorBuilder().addModifiers(KModifier.PUBLIC)
        generateScreenNameLog(function, screen)
        implClass?.addFunction(function.build())
    }

    private fun createFile(className: String, pkgName: String, type: TypeSpec?) {
        val kaptGeneratedDirPath = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
            ?.replace("kaptKotlin", "kapt")
            ?: run {
                processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, "Can't find the target directory for generated Kotlin files.")
                return
            }

        val kaptGeneratedDir = File(kaptGeneratedDirPath)
        if (!kaptGeneratedDir.parentFile.exists()) {
            kaptGeneratedDir.parentFile.mkdirs()
        }

        val file = FileSpec.builder(pkgName, className)
            .addComment(FILE_COMMENT)
            .addType(type!!)
            .build()
        file.writeTo(kaptGeneratedDir)
    }
}