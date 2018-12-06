package goldzweigapps.com.compiler.models

import com.squareup.kotlinpoet.ClassName
import goldzweigapps.com.compiler.consts.Packages

/**
 * Created by gilgoldzweig on 08/02/2018.
 */
data class ViewType(val layoutName: String = "",
                    val dataContainerType: ClassName,
                    val viewHolderType: ClassName)

