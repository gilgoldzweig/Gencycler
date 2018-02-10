package goldzweigapps.com.annotations.annotations

import kotlin.reflect.KClass

/**
 * Created by gilgoldzweig on 08/02/2018.
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class NamingAdapter(val adapter: KClass<out Any>)