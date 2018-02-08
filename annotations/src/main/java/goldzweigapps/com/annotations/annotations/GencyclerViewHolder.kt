package goldzweigapps.com.annotations.annotations

import kotlin.reflect.KClass

/**
 * Created by gilgoldzweig on 12/10/2017.
 */

data class GencyclerHolderImpl(val layoutName: String,
                               val viewFields: List<GencyclerViewImpl>,
                               val classType: String,
                               val rClass: String)