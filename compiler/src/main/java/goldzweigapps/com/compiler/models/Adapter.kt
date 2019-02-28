package goldzweigapps.com.compiler.models

import com.squareup.kotlinpoet.TypeSpec
import goldzweigapps.com.annotations.annotations.Actions


/**
 * Created by gilgoldzweig on 08/02/2018.
 */
data class Adapter(val name: String,
				   val packageName: String,
                   val viewTypes: List<ViewType>,
				   val actions: Array<out Actions>,
				   val listenerInterface: TypeSpec? = null)