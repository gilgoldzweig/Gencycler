package goldzweigapps.com.compiler.utils

import com.google.common.base.CaseFormat
import com.squareup.kotlinpoet.ClassName

val ClassName.simpleEnumName: String
    get() = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, simpleName)

val ClassName.simpleParameterName: String
    get() = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, simpleName)