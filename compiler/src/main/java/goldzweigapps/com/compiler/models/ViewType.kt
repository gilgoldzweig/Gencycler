package goldzweigapps.com.compiler.models

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import goldzweigapps.com.annotations.annotations.Actions

/**
 * Created by gilgoldzweig on 08/02/2018.
 */
data class ViewType(val layoutName: String = "",
                    val dataContainerType: TypeName,
                    val viewHolderType: ClassName,
                    var actions: Set<Actions> = emptySet())

