package goldzweigapps.com.annotations.annotations

import kotlin.reflect.KClass

/**
 * Created by gilgoldzweig on 12/10/2017.
 */
@Retention(AnnotationRetention.SOURCE)
annotation class GencyclerViewHolder(val layoutResId: Int,
                                     vararg val viewFields: GencyclerViewField,
                                     val classType: KClass<out Any>)
class GencyclerViewHolderImpl(val layoutResId: Int,
                              vararg val viewFields: GencyclerViewFieldImpl,
                              val classType: String)