package goldzweigapps.com.annotations.annotations

import kotlin.reflect.KClass

/**
 * Created by gilgoldzweig on 25/10/2017.
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Holder(val layoutName: String,
                        vararg val recyclerAdapters: KClass<out Any>)

