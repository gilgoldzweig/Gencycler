package goldzweigapps.com.compiler.adapter

import goldzweigapps.com.annotations.annotations.*

/**
 * Created by gilgoldzweig on 09/02/2018.
 */
open class NamingAdapter {
    open fun buildNameForId(id: String): String = id
}

fun getNamingAdapter(namingCase: NamingCase): NamingAdapter {
    return when (namingCase) {
        NamingCase.NAMING_CASE_NONE ->
            NamingAdapter()

        NamingCase.NAMING_CASE_CAMEL ->
            CamelCaseNamingAdapter()

        NamingCase.NAMING_CASE_SNAKE ->
            SnakeCaseNamingAdapter()

    }
}