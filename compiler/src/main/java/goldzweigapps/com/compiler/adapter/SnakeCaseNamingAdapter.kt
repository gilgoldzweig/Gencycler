package goldzweigapps.com.compiler.adapter

import com.google.common.base.CaseFormat

/**
 * Created by gilgoldzweig on 08/02/2018.
 */
class SnakeCaseNamingAdapter : NamingAdapter() {

    override fun buildNameForId(id: String): String =
            CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, id)
}