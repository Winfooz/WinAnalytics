package com.winfooz.winanalytics.compiler.types

import com.squareup.kotlinpoet.*
import com.winfooz.winanalytics.annotations.Analytics
import com.winfooz.winanalytics.compiler.*
import com.winfooz.winanalytics.compiler.models.Configuration
import com.winfooz.winanalytics.compiler.models.FieldData
import java.io.File
import javax.annotation.processing.ProcessingEnvironment

/**
 * For generate class for every class that contains class field annotated with [Analytics]
 *
 * @param processingEnv for use processing utils like messager for log errors or warnings
 *                      or use filer to create kotlin or java files etc...
 * @param pkg The package that contains class which has class fields annotated with [Analytics]
 *            annotation for generate class in the same package.
 * @param className The class reference for generate analytics class with same reference + "_Analytics" suffix
 * @param configuration This configuration for generate methods based on configurations.
 * @param configurationType This type for generate class with reference "Analytics" contains every
 *                          object of analytics classes.
 */
class AnalyticsType(
        private val processingEnv: ProcessingEnvironment,
        private val pkg: String,
        private val className: String,
        private val configuration: Configuration,
        configurationType: ConfigurationType?
) : Type {

    /**
     * Methods for generate every value method with multiple client support.
     */
    private val methods = mutableMapOf<String, Pair<String, MutableList<Pair<String, String>>>>()

    /**
     * For add current analytics class object to Analytics class to use from everywhere
     * with singleton access.
     */
    init {
        configurationType?.addAnalytics(pkg, className, className + ANALYTICS_CLASS_NAME_SUFIX)
    }

    /**
     * Type builder for current analytics class
     */
    private val typBuilder: TypeSpec.Builder = TypeSpec.classBuilder("$className$ANALYTICS_CLASS_NAME_SUFIX")

    /**
     * For generate constructor for current analytics class with context and clients parameter
     * based on configuration
     *
     * @see [Configuration]
     */
    private val constructorBuilder: FunSpec.Builder by lazy {
        val funSpec = FunSpec.constructorBuilder()
        funSpec.addModifiers(KModifier.PUBLIC)
        funSpec.addParameter(ParameterSpec.builder("context", CONTEXT, KModifier.PRIVATE).build())
        typBuilder.addProperty(PropertySpec.builder("context", CONTEXT, KModifier.PRIVATE).initializer("context").build())
        addPram(configuration.firebaseEnabled, funSpec, FIREBASE_PREFIX, FIREBASE_ANALYTICS)
        addPram(configuration.fabricEnabled, funSpec, FABRIC_PREFIX, FABRIC_ANALYTICS)
        addPram(configuration.mixPanelEnabled, funSpec, MIXPANEL_PREFIX, MIXPANEL_ANALYTICS)
        funSpec
    }

    /**
     * For add method to [AnalyticsType.methods] object for every value with multiple client support.
     *
     * @see methods
     * @see Configuration.any
     */
    override fun addStatement(data: FieldData) {
        (data.type as Analytics).events.forEach {
            addAnalyticsMethod(configuration.any(), it.value, it.value.toCamelCase(), data)
            addAnalyticsMethod(configuration.firebaseEnabled, it.value, it.value.toCamelCase() + FIREBASE_PREFIX, data)
            addAnalyticsMethod(configuration.fabricEnabled, it.value, it.value.toCamelCase() + FABRIC_PREFIX, data)
            addAnalyticsMethod(configuration.mixPanelEnabled, it.value, it.value.toCamelCase() + MIXPANEL_PREFIX, data)
        }
    }

    /**
     * For generate class with all methods that added to [methods] object based on [configuration]
     *
     * @see Configuration
     * @see BindingFactory.bindAnalytics
     * @see FileSpec
     * @see File
     */
    override fun build() {
        BindingFactory.bindAnalytics(typBuilder, pkg, className, methods, configuration)
        val fileName = "$className$ANALYTICS_CLASS_NAME_SUFIX"
        typBuilder.primaryConstructor(constructorBuilder.build())
        val file = FileSpec.builder(pkg, fileName)
                .addType(typBuilder.build())
                .build()
        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        file.writeTo(File(kaptKotlinGeneratedDir, fileName))
    }

    /**
     * For add method to [methods] object or add statement to method if already exist in [methods]
     * object.
     *
     * @param enabled for check if clients enabled or not.
     * @param name the method reference
     * @param data the class field for add to log value.
     */
    private fun addAnalyticsMethod(enabled: Boolean, event: String, name: String, data: FieldData) {
        if (enabled) {
            if (methods[name] == null) {
                methods[name] = Pair(event, mutableListOf(Pair(data.eventName, data.reference)))
            } else {
                methods[name]?.second?.add(Pair(data.eventName, data.reference))
            }
        }
    }

    /**
     * For add parameter to function based on [add] param
     *
     * @param add for check if can add parameter on not based on [configuration].
     * @param name the parameter reference.
     * @param className the parameter value.
     *
     * @see FunSpec.Builder
     */
    private fun addPram(add: Boolean, funSpec: FunSpec.Builder, name: String, className: ClassName) {
        if (add) {
            typBuilder.addProperty(PropertySpec.builder(name.toLowerCase(), className, KModifier.PRIVATE).initializer(name.toLowerCase()).build())
            funSpec.addParameter(name.toLowerCase(), className, KModifier.PRIVATE)
        }
    }
}