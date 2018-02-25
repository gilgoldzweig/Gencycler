package goldzweigapps.com.annotations.annotations

import android.support.annotation.LayoutRes
import kotlin.reflect.KClass

/**
 * Created by gilgoldzweig on 25/10/2017.
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Holder(val layoutName: String,
                        @LayoutRes val layoutRes: Int,
                        vararg val recyclerAdapters: KClass<out Any>)

