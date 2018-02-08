package goldzweigapps.com.annotations.annotations

import kotlin.reflect.KClass


/**
 * Created by gilgoldzweig on 12/10/2017.
 */
@Retention(AnnotationRetention.SOURCE)
annotation class GencyclerViewField(val name: String = "",
                                    val resId: Int = 0,
                                    val viewType: KClass<out Any>)

data class GencyclerViewFieldImpl(val name: String = "",
                             val resId: Int = 0,
                             val viewType: String)

data class GencyclerViewImpl(val name: String = "",
                             val resId: String,
                             val viewType: String,
                             val field: GencyclerField? = null)
