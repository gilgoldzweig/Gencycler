package goldzweigapps.com.compiler.models

import com.squareup.kotlinpoet.ClassName
import goldzweigapps.com.compiler.adapter.NamingAdapter
import goldzweigapps.com.compiler.consts.Packages


/**
 * Created by gilgoldzweig on 08/02/2018.
 */
data class ViewHolder(val name: String,
                      val layoutName: String,
                      val namingAdapter: NamingAdapter,
                      val viewFields: List<ViewField> = emptyList()) {

    internal var className: ClassName? = null
}

fun ViewHolder.asClassName(): ClassName {
    if (className == null) {
        className = ClassName.bestGuess("${Packages.GENCYCLER}.$name")
    }

    return className!!
}
