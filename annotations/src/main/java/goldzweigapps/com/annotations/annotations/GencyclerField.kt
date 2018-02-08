package goldzweigapps.com.annotations.annotations

import android.support.annotation.IdRes

/**
 * Created by gilgoldzweig on 05/11/2017.
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class GencyclerField(@IdRes val viewId: Int, val filedType: GencyclerFieldType)

enum class GencyclerFieldType {
    SRC, BACKGROUND, TEXT
}