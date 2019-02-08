package com.winfooz

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeSpec
import com.winfooz.elements.AnalyticsElement
import com.winfooz.elements.AnalyticsWrapperElement
import com.winfooz.elements.AnalyticsWrapperMethod
import com.winfooz.elements.EventElement
import com.winfooz.elements.EventWithClickElement
import com.winfooz.elements.ScreenElement
import java.io.IOException
import javax.annotation.processing.Filer
import javax.lang.model.element.Modifier
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType

/**
 * Project: WinAnalytics2
 * Created: November 16, 2018
 *
 * @author Mohamed Hamdan
 */
class JavaProcessor(
    private var filer: Filer,
    private var analyticsWrapperElements: MutableSet<AnalyticsWrapperElement>,
    private var screenElements: MutableSet<ScreenElement>,
    private var analyticsElements: MutableSet<AnalyticsElement>,
    private var eventWithClickElements: MutableSet<EventWithClickElement>,
    private var winAnalyticsIndex: String?
) : Processor {

    override fun process() {
        generateAnalytics()
        generateAnalyticsWrappers()
        generateEventWithClick()
        generateScreens()
        generateIndex()
    }

    private fun generateAnalyticsWrappers() {
        analyticsWrapperElements.forEach { element ->
            val implClass = TypeSpec.classBuilder("${element.className}_Impl")
                .addSuperinterface(ClassName.get(element.pkgName, element.className))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            element.methods.forEach {
                val method = generateAnalyticsWrapperMethod(implClass, it)
                method?.let {
                    implClass.addMethod(method)
                }
            }

            try {
                val javaFile = JavaFile.builder(element.pkgName, implClass.build()).build()
                javaFile.writeTo(filer)
            } catch (ignored: IOException) {
            }
        }
    }

    private fun generateAnalytics() {
        analyticsElements.forEach { element ->
            val implClass = TypeSpec.classBuilder("${element.className}_Impl")
                .addSuperinterface(ClassName.get(element.pkgName, element.className))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            element.events.forEach { event ->
                val method = generateAnalyticsMethod(event, element)
                method?.let {
                    implClass.addMethod(it)
                }
            }

            try {
                val javaFile = JavaFile.builder(element.pkgName, implClass.build()).build()
                javaFile.writeTo(filer)
            } catch (ignored: IOException) {
            }
        }
    }

    private fun generateAnalyticsMethod(event: EventElement, element: AnalyticsElement): MethodSpec? {
        return try {
            val method = MethodSpec.overriding(event.element)
            method.addStatement(
                "\$T<\$T, \$T<\$T, \$T>> pair = new \$T<>(\$S, new \$T<>())",
                PAIR_CLASS_NAME,
                String::class.java,
                HASH_MAP_CLASS_NAME,
                String::class.java,
                Object::class.java,
                PAIR_CLASS_NAME,
                event.eventName,
                HASH_MAP_CLASS_NAME
            )
            val date = if (event.data.isNotEmpty()) {
                event.data
            } else {
                element.data.addAll(event.addedData)
                element.data.filter { data ->
                    event.removedData.all { it.value != data.data.key.value }
                }
            }
            date.forEach {
                method.addStatement(
                    "\$N.put(\$S, \$N)",
                    "pair.second",
                    it.name,
                    it.reference
                )
            }
            if (event.event.timestamp || element.analytics.timestamp) {
                method.addStatement(
                    "\$N.put(\$S, \$N)",
                    "pair.second",
                    "timestamp",
                    "System.currentTimeMillis()"
                )
            }
            method.beginControlFlow(
                "\nfor(\$T adapter : \$T.\$N().\$N())",
                ANALYTICS_ADAPTER_CLASS_NAME,
                ANALYTICS_CLASS_NAME,
                "getInstance().getConfiguration",
                "getAdapters"
            )
            method.addStatement(
                "\$N.log(\$N)",
                "adapter",
                "pair"
            )
            method.endControlFlow()
            method.build()
        } catch (e: Exception) {
            null
        }
    }

    private fun generateAnalyticsWrapperMethod(implClass: TypeSpec.Builder, analyticsMethod: AnalyticsWrapperMethod): MethodSpec? {
        val method = MethodSpec.overriding(analyticsMethod.element)
        return try {
            val packageName = ((analyticsMethod.element.returnType as DeclaredType).asElement().enclosingElement as PackageElement).qualifiedName.toString()
            val className = (analyticsMethod.element.returnType as DeclaredType).asElement().simpleName.toString()
            implClass.addField(FieldSpec.builder(ClassName.get(packageName, className), className.toCamelCase(), Modifier.PRIVATE).build())
            method.addCode("if (${className.toCamelCase()} == null) {\n")
            method.addStatement("\$N = new \$T()", className.toCamelCase(), ClassName.get(packageName, "${className}_Impl"))
            method.addCode("}\n")
            method.addStatement("return \$N", className.toCamelCase())
            method.build()
        } catch (e: Exception) {
            null
        }
    }

    private fun generateEventWithClick() {
        val groups = eventWithClickElements.groupBy { "${it.pkgName},${it.className}" }
        groups.keys.forEach {
            val className = it.split(",")[1]
            val pkgName = it.split(",")[0]
            val implClass = TypeSpec.classBuilder("${className}_Analytics")
                .addSuperinterface(DESTROYABLE_CLASS_NAME)
                .addAnnotation(KEEP_CLASS_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)

            addClassMembers(implClass, groups[it]?.toMutableList())
            addEventWithClickConstructor(className, groups[it]?.toMutableList(), pkgName, implClass)

            overrideDestroyMethod(groups[it]?.toMutableList(), implClass)
            try {
                val javaFile = JavaFile.builder(pkgName, implClass.build()).build()
                javaFile.writeTo(filer)
            } catch (ignored: IOException) {
            }
        }
    }

    private fun overrideDestroyMethod(fields: MutableList<EventWithClickElement>?, implClass: TypeSpec.Builder?) {
        val method = MethodSpec.methodBuilder("destroy")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override::class.java)

        method.beginControlFlow("if (target == null)")
        method.addStatement("throw new \$T(\$S)", IllegalStateException::class.java, "Already destroyed")
        method.endControlFlow()
        fields?.forEach {
            method.addStatement("${it.methodName}.setOnClickListener(null)")
            method.addStatement("${it.methodName} = null")
            method.addCode("\n")
        }
        method.addStatement("target = null")
        implClass?.addMethod(method.build())
    }

    private fun addEventWithClickConstructor(className: String, fields: MutableList<EventWithClickElement>?, pkgName: String, implClass: TypeSpec.Builder?) {
        val method = MethodSpec.constructorBuilder()
            .addParameter(ParameterSpec.builder(ClassName.get(pkgName, className), "target", Modifier.FINAL).build())
            .addParameter(ParameterSpec.builder(VIEW_CLASS_NAME, "view", Modifier.FINAL).build())
            .addStatement("this.target = target")
            .addModifiers(Modifier.PUBLIC)
        implClass?.addField(ClassName.get(pkgName, className), "target", Modifier.PRIVATE)
        fields?.forEach { event ->
            event.analyticsElement?.pkgName?.let { pkgName ->
                if (implClass?.build()?.fieldSpecs?.map { it.name }?.contains(event.analyticsElement?.className?.toCamelCase()) != true) {
                    implClass?.addField(ClassName.get(pkgName, event.analyticsElement?.className), event.analyticsElement?.className?.toCamelCase(), Modifier.PRIVATE)
                }
                method.addStatement("\$N = view.findViewById(\$L)", event.methodName, event.eventOnClick.value)
                method.addCode("\$N.setOnClickListener(new \$T() {\n", event.methodName, ON_CLICK_LISTENER_CLASS_NAME)
                method.addCode("  @Override\n", event.methodName, ON_CLICK_LISTENER_CLASS_NAME)
                method.addCode("  public void onClick(\$T v) {\n", VIEW_CLASS_NAME)
                method.addCode("    if (${event.analyticsElement?.className?.toCamelCase()} == null) {\n")
                method.addCode("      ${event.analyticsElement?.className?.toCamelCase()} = new ${event.analyticsElement?.className}_Impl();\n")
                method.addCode("    }\n")
                method.addCode("    target.${event.methodName}();\n")
                method.addCode("    ${event.analyticsElement?.className?.toCamelCase()}.${event.analyticsEvent?.methodName}(${event.parameters.joinToString { param -> "target.${param?.simpleName}" }});\n")
                method.addCode("  }\n});\n")
            }
        }
        val screenElement = screenElements.find { "${it.pkgName}.${it.className}" == "$pkgName.$className" }
        screenElements.remove(screenElement)
        generateScreenNameLog(method, screenElement)
        implClass?.addMethod(method.build())
    }

    private fun addClassMembers(implClass: TypeSpec.Builder?, fields: MutableList<EventWithClickElement>?) {
        fields?.forEach {
            implClass?.addField(VIEW_CLASS_NAME, it.methodName, Modifier.PRIVATE)
        }
    }

    private fun generateScreenNameLog(method: MethodSpec.Builder, screen: ScreenElement?) {
        if (screen != null) {
            method.addStatement(
                "\$T<\$T, \$T<\$T, \$T>> pair = new \$T<>(\$S, new \$T<>())",
                PAIR_CLASS_NAME,
                String::class.java,
                HASH_MAP_CLASS_NAME,
                String::class.java,
                Object::class.java,
                PAIR_CLASS_NAME,
                screen.screen?.value,
                HASH_MAP_CLASS_NAME
            )
            method.addStatement(
                "\$N.put(\$S, \$S)",
                "pair.second",
                "screen_name",
                if (screen.screen?.value == "") screen.className else screen.screen?.value
            )
            if (screen.screen?.timestamp == true) {
                method.addStatement(
                    "\$N.put(\$S, \$N)",
                    "pair.second",
                    "timestamp",
                    "System.currentTimeMillis()"
                )
            }
            method.beginControlFlow(
                "\nfor(\$T adapter : \$T.\$N().\$N())",
                ANALYTICS_ADAPTER_CLASS_NAME,
                ANALYTICS_CLASS_NAME,
                "getInstance().getConfiguration",
                "getAdapters"
            )
            method.addStatement(
                "\$N.log(\$N)",
                "adapter",
                "pair"
            )
            method.endControlFlow()
            method.build()
        }
    }

    private fun generateScreens() {
        screenElements.forEach {
            val implClass = TypeSpec.classBuilder("${it.className}_Analytics")
                .addSuperinterface(DESTROYABLE_CLASS_NAME)
                .addAnnotation(KEEP_CLASS_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            overrideScreenDestroyMethod(implClass)
            addScreenConstructor(it, implClass)
            try {
                val javaFile = JavaFile.builder(it.pkgName, implClass.build()).build()
                javaFile.writeTo(filer)
            } catch (ignored: IOException) {
            }
        }
    }

    private fun overrideScreenDestroyMethod(implClass: TypeSpec.Builder?) {
        val method = MethodSpec.methodBuilder("destroy")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override::class.java)
        implClass?.addMethod(method.build())
    }

    private fun addScreenConstructor(screen: ScreenElement, implClass: TypeSpec.Builder?) {
        val method = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
        generateScreenNameLog(method, screen)
        implClass?.addMethod(method.build())
    }

    private fun generateIndex() {
        winAnalyticsIndex?.let {
            try {
                val lastIndex = it.lastIndexOf('.')
                val implClass = TypeSpec.classBuilder(it.substring(lastIndex + 1))
                    .addAnnotation(KEEP_CLASS_NAME)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)

                addIndexConstructor(implClass)
                addIndexEventsGetterMethod(implClass)

                val javaFile = JavaFile.builder(it.substring(0, lastIndex), implClass.build()).build()
                javaFile.writeTo(filer)
            } catch (ignored: IOException) {
            }
        }
    }

    private fun addIndexConstructor(implClass: TypeSpec.Builder?) {
        val method = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addStatement("events = new \$T<>()", HashMap::class.java)

        val successes = analyticsElements.flatMap { it.successes }
        val failures = analyticsElements.flatMap { it.failures }

        implClass?.addField(FieldSpec.builder(ParameterizedTypeName.get(HASH_MAP_CLASS_NAME, STRING_CLASS_NAME, HTTP_EVENT_CLASS_NAME), "events", Modifier.PRIVATE).build())

        method.beginControlFlow("try")
        successes.forEach {
            method.addCode("events.put(\$S, new \$T(\$S, \$S, \$S, \$S", "${it.call.value}:success", HTTP_EVENT_CLASS_NAME, it.pkgName, "${it.className}_Impl", it.methodName, it.name)
            if (it.parameters.size > 0) {
                it.parameters.forEach { element ->
                    method.addCode(",\$T.forName(\$S)", Class::class.java, ((element.asType() as DeclaredType).asElement() as TypeElement).qualifiedName.toString())
                }
            }
            method.addCode("));\n")
        }

        failures.forEach {
            method.addCode("events.put(\$S, new \$T(\$S, \$S, \$S, \$S", "${it.call.value}:failure", HTTP_EVENT_CLASS_NAME, it.pkgName, "${it.className}_Impl", it.methodName, it.name)
            if (it.parameters.size > 0) {
                it.parameters.forEach { element ->
                    method.addCode(",\$T.forName(\$S)", Class::class.java, ((element.asType() as DeclaredType).asElement() as TypeElement).qualifiedName.toString())
                }
            }
            method.addCode("));\n")
        }
        method.endControlFlow()
        method.beginControlFlow("catch(\$T ignored)", java.lang.Exception::class.java)
        method.endControlFlow()
        implClass?.addMethod(method.build())
    }

    private fun addIndexEventsGetterMethod(implClass: TypeSpec.Builder?) {
        val method = MethodSpec.methodBuilder("getEvents")
            .returns(ParameterizedTypeName.get(HASH_MAP_CLASS_NAME, STRING_CLASS_NAME, HTTP_EVENT_CLASS_NAME))
            .addStatement("return events")
            .addModifiers(Modifier.PUBLIC)
            .build()

        implClass?.addMethod(method)
    }
}