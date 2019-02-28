package goldzweigapps.com.compiler.utils

import com.google.common.base.CaseFormat
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName

val ClassName.simpleEnumName: String
    get() = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, simpleName)

val ClassName.simpleParameterName: String
    get() = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, simpleName)

val ParameterizedTypeName.simpleParameterName: String
    get() = rawType.simpleParameterName


val TypeName.simpleParameterName: String
    get() = when (this) {
        is ClassName ->
            simpleParameterName

        is ParameterizedTypeName ->
            simpleParameterName

        else ->
            throw IllegalArgumentException("Unexpected type was found")

    }
