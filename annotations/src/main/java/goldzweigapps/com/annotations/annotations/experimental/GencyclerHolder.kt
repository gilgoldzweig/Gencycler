package goldzweigapps.com.annotations.annotations.experimental

import goldzweigapps.com.annotations.annotations.GencyclerViewImpl
import goldzweigapps.com.annotations.interfaces.GencyclerDataType
import kotlin.reflect.KClass

/**
 * Created by gilgoldzweig on 25/10/2017.
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class GencyclerHolder(val layoutName: String,
                                 vararg val adapters: KClass<out Any>)

data class GencyclerHolderImpl(val layoutName: String, val dataType: String, val rClass: String)
