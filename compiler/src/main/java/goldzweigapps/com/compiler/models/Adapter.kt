package goldzweigapps.com.compiler.models


/**
 * Created by gilgoldzweig on 08/02/2018.
 */
data class Adapter(val name: String,
                   val viewTypes: List<ViewType>,
				   val clickable: Boolean = false,
				   val longClickable: Boolean = false,
				   val filterable: Boolean = false)