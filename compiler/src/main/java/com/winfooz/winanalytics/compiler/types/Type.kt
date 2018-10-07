package com.winfooz.winanalytics.compiler.types

import com.winfooz.winanalytics.compiler.models.FieldData

/**
 * Every type should implement this interface for generate classes for statements
 */
interface Type {

    /**
     * For add statement or method or fields
     */
    fun addStatement(data: FieldData)

    /**
     * For create class with all generated statements
     */
    fun build()
}