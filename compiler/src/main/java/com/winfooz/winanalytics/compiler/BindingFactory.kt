package com.winfooz.winanalytics.compiler

import com.squareup.kotlinpoet.*
import com.winfooz.winanalytics.annotations.Analytics
import com.winfooz.winanalytics.annotations.AnalyticsTypes.*
import com.winfooz.winanalytics.annotations.AnalyticsConfiguration
import com.winfooz.winanalytics.compiler.models.Configuration
import com.winfooz.winanalytics.compiler.types.AnalyticsType
import com.winfooz.winanalytics.compiler.types.ConfigurationType

/**
 * For generate the types methods like analytics lazy val
 */
object BindingFactory {

    /**
     * This method for generate analytics functions that contains every event and values.
     *
     * @param classBuilder The analytics class.
     * @param pkg The package that contains class which has class fields annotated with [Analytics]
     *            annotation for import class in the generated analytics class.
     * @param className The class reference for add it as parameter for every generated method.
     * @param methods The methods which need to be generated inside [classBuilder].
     * @param configurationData The configuration for enable or disable multiple client generation.
     *
     * @see AnalyticsType
     */
    fun bindAnalytics(classBuilder: TypeSpec.Builder, pkg: String, className: String,
                      methods: MutableMap<String, Pair<String, MutableList<Pair<String, String>>>>,
                      configurationData: Configuration) {
        methods.keys.forEach { key ->
            val function = FunSpec.builder(MethodsUtil.getAnalyticsMethod(key))
            function.addParameter("instance", ClassName(pkg, className))
            if (isFirebaseMethod(key) || isFabricMethod(key) || isMixpanelMethod(key)) {
                function.addStatement(
                        "val pair: Pair<%T, %N<%T, %T>> = %T(%S, %N)",
                        String::class,
                        "MutableMap",
                        String::class,
                        String::class,
                        Pair::class, methods[key]?.first ?: "",
                        "mutableMapOf()"
                )
                methods[key]?.second?.forEach {
                    function.addStatement("%N.%N.%N(%S, %N)", "pair", "second", "put", it.first, "instance.${it.second}.toString()")
                }
                if (isFirebaseMethod(key)) {
                    function.addStatement("%N.log(%N)", FIREBASE_PREFIX.toLowerCase(), "pair")
                }
                if (isFabricMethod(key)) {
                    function.addStatement("%N.log(%N)", FABRIC_PREFIX.toLowerCase(), "pair")
                }
                if (isMixpanelMethod(key)) {
                    function.addStatement("%N.log(%N)", MIXPANEL_PREFIX.toLowerCase(), "pair")
                }
            } else {
                if (configurationData.firebaseEnabled) {
                    function.addStatement("$key${FIREBASE_PREFIX}Event(instance)")
                }
                if (configurationData.mixPanelEnabled) {
                    function.addStatement("$key${MIXPANEL_PREFIX}Event(instance)")
                }
                if (configurationData.fabricEnabled) {
                    function.addStatement("$key${FABRIC_PREFIX}Event(instance)")
                }
            }
            classBuilder.addFunction(function.build())
        }
    }

    /**
     * This method for generate enabled clients with lazy initialization inside Analytics class
     *
     * @param type For read configuration values by [AnalyticsConfiguration] annotation.
     * @param configurationData For set configuration data bases on [configurationData] values.
     */
    fun bindAnalyticsConfiguration(type: Annotation, configurationData: Configuration): TypeSpec.Builder {
        val configuration = type as AnalyticsConfiguration
        configurationData.className = configuration.className
        val typeSpec = TypeSpec.classBuilder(configuration.className)
        configuration.value.forEach {
            when (it.type) {
                FIREBASE -> {
                    if (it.enabled) {
                        configurationData.firebaseEnabled = true
                        addClient(typeSpec, FIREBASE_PREFIX.toLowerCase(), it.key, FIREBASE_ANALYTICS)
                    }
                }
                FABRIC -> {
                    if (it.enabled) {
                        configurationData.fabricEnabled = true
                        addClient(typeSpec, FABRIC_PREFIX.toLowerCase(), it.key, FABRIC_ANALYTICS)
                    }
                }
                MIXPANEL -> {
                    if (it.enabled) {
                        configurationData.mixPanelEnabled = true
                        configurationData.mixpanelKey = it.key
                        addClient(typeSpec, MIXPANEL_PREFIX.toLowerCase(), it.key, MIXPANEL_ANALYTICS)
                    }
                }
            }
        }
        return typeSpec
    }

    /**
     * For add lazy initialization for every generated analytics class inside Analytics class
     *
     * @param typeSpec the [ConfigurationType].
     * @param pkg The package that contains class which has class fields annotated with [Analytics]
     *            annotation for import class in the generated analytics class.
     * @param className The class reference for know which class in the [pkg] need to import.
     * @param name The field reference
     * @param configuration The configuration for enable or disable multiple client generation.
     */
    fun bindAnalyticsConfigurationAnalyticsObjects(typeSpec: TypeSpec.Builder, pkg: String, name: String, className: String, configuration: Configuration) {
        val codeBlock = CodeBlock.builder()
        codeBlock.beginControlFlow("lazy")
        codeBlock.add("%T(", ClassName(pkg, className))
        codeBlock.add("context")
        if (configuration.firebaseEnabled) {
            codeBlock.add(",")
            codeBlock.add(FIREBASE_PREFIX.toLowerCase())
        }
        if (configuration.fabricEnabled) {
            codeBlock.add(",")
            codeBlock.add(FABRIC_PREFIX.toLowerCase())
        }
        if (configuration.mixPanelEnabled) {
            codeBlock.add(",")
            codeBlock.add(MIXPANEL_PREFIX.toLowerCase())
        }
        codeBlock.add(")")
        codeBlock.endControlFlow()
        typeSpec.addProperty(
                PropertySpec.builder(name[0].toLowerCase() + name.substring(1) + "Analytics", ClassName(pkg, className), KModifier.PUBLIC)
                        .delegate(codeBlock.build())
                        .build()
        )
    }

    /**
     * For add supported and enabled client to the Analytics class
     *
     * @param typeSpec the [ConfigurationType].
     * @param name The client reference
     * @param className The client class path for import.
     */
    private fun addClient(typeSpec: TypeSpec.Builder, name: String, key: String, className: ClassName) {
        val codeBlock = CodeBlock.builder()
        codeBlock.beginControlFlow("lazy")
        codeBlock.add("%T(context", className)
        if (name.toLowerCase() == MIXPANEL_PREFIX.toLowerCase()) {
            codeBlock.add(",%S", key)
        }
        codeBlock.add(")")
        codeBlock.endControlFlow()
        typeSpec.addProperty(
                PropertySpec.builder(name, className, KModifier.PRIVATE)
                        .delegate(codeBlock.build())
                        .build()
        )
    }

    /**
     * For check if method contains [FIREBASE_PREFIX]
     */
    private fun isFirebaseMethod(key: String): Boolean = MethodsUtil.getAnalyticsMethod(key).contains(FIREBASE_PREFIX, true)

    /**
     * For check if method contains [FABRIC_PREFIX]
     */
    private fun isFabricMethod(key: String): Boolean = MethodsUtil.getAnalyticsMethod(key).contains(FABRIC_PREFIX, true)

    /**
     * For check if method contains [MIXPANEL_PREFIX]
     */
    private fun isMixpanelMethod(key: String): Boolean = MethodsUtil.getAnalyticsMethod(key).contains(MIXPANEL_PREFIX, true)

    /**
     * For remove clients names from the method reference
     */
    private fun getEventName(key: String): String = key.replace(FIREBASE_PREFIX, "", true)
            .replace(MIXPANEL_PREFIX, "", true)
            .replace(FABRIC_PREFIX, "", true)
}
