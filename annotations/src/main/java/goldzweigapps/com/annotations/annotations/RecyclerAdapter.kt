package goldzweigapps.com.annotations.annotations

import kotlin.reflect.KClass

/**
 * Created by gilgoldzweig on 12/10/2017.
 *
 */
@Retention(AnnotationRetention.SOURCE)
annotation class RecyclerAdapter(val rClass: KClass<*>, val customName: String = "")