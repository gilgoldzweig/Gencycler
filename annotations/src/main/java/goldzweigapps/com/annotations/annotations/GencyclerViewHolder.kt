package goldzweigapps.com.annotations.annotations

import kotlin.reflect.KClass

/**
 * Created by gilgoldzweig on 12/10/2017.
 */
@Retention(AnnotationRetention.SOURCE)
annotation class GencyclerViewHolder(val layoutResId: Int,
                                     vararg val viewFields: GencyclerViewField,
                                     val classType: KClass<out Any>)

@Retention(AnnotationRetention.SOURCE)
annotation class GencyclerBetaViewHolder(val layoutFileName: String,
                                     val classType: KClass<out Any>)

class GencyclerViewHolderImpl(val layoutResId: Int,
                              vararg val viewFields: GencyclerViewFieldImpl,
                              val classType: String)

data class GencyclerHolderImpl(val layoutName: String,
                               val viewFields: List<GencyclerViewImpl>,
                               val classType: String,
                               val rClass: String)