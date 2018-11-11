package com.winfooz.winanalytics.compiler.types

import com.squareup.kotlinpoet.*
import com.winfooz.winanalytics.annotations.Analytics
import com.winfooz.winanalytics.compiler.*
import com.winfooz.winanalytics.compiler.models.Configuration
import com.winfooz.winanalytics.compiler.models.FieldData
import java.io.File
import javax.annotation.processing.ProcessingEnvironment

/**
 * This type for generate class with reference "Analytics" contains every object of analytics classes.
 *
 * @param processingEnv for use processing utils like messager for log errors or warnings
 *                      or use filer to create kotlin or java files etc...
 * @param pkg The package that contains class which has class fields annotated with [Analytics]
 *            annotation for generate class in the same package.
 * @param configuration This configuration for generate methods based on configurations.
 */
class ConfigurationType(private val processingEnv: ProcessingEnvironment,
                        private val pkg: String,
                        private val configuration: Configuration) : Type {

    /**
     * Type builder for current analytics class
     */
    private lateinit var typBuilder: TypeSpec.Builder

    /**
     * For generate companion object that extends SingletonHolder class for implement
     * singleton pattern with context parameter
     */
    private val singletonHolder: TypeSpec.Builder by lazy {
        TypeSpec.companionObjectBuilder()
                .superclass(ParameterizedTypeName.get(
                        SINGLETON_HOLDER,
                        ClassName(pkg, configuration.className),
                        CONTEXT
                ))
                .addSuperclassConstructorParameter("::${configuration.className}")
    }


    /**
     * For generate private constructor with context parameter
     */
    private val constructorBuilder: FunSpec.Builder = FunSpec.constructorBuilder()
            .addModifiers(KModifier.PRIVATE)
            .addParameter(ParameterSpec.builder(
                    "context",
                    CONTEXT,
                    KModifier.PRIVATE
            ).build())

    /**
     * For add supported clients objects with lazy initialization and generate clients keys
     */
    override fun addStatement(data: FieldData) {
        typBuilder = BindingFactory.bindAnalyticsConfiguration( data.type, configuration)
    }

    /**
     * For generate class fields with lazy initialization for every analytics class
     */
    fun addAnalytics(pkg: String, name: String, className: String) {
        BindingFactory.bindAnalyticsConfigurationAnalyticsObjects(typBuilder, pkg, name, className, configuration)
    }

    /**
     * For generate Analytics class with singleton pattern
     */
    override fun build() {
        typBuilder.addProperty(PropertySpec.builder("context", CONTEXT, KModifier.PRIVATE).initializer("context").build())
        typBuilder.primaryConstructor(constructorBuilder.build())
        typBuilder.addType(singletonHolder.build())
        val file = FileSpec.builder(pkg, configuration.className)
        file.addType(typBuilder.build())
        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        file.build().writeTo(File(kaptKotlinGeneratedDir, configuration.className))
    }
}