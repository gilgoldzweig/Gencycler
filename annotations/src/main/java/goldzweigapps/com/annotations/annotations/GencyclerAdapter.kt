package goldzweigapps.com.annotations.annotations

import android.support.annotation.IntDef
import android.support.annotation.LayoutRes
import kotlin.reflect.KClass


/**
 * Informs the Gencycler code generator what viewHolders it wants to have
 *
 * @param value our data containers which extends [GencyclerDataContainer]
 * and they are annotated with the [GencyclerViewHolder] annotation
 *
 * @see GencyclerViewHolder
 * @see GencyclerDataContainer
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
@Repeatable
annotation class GencyclerAdapter(vararg val value: KClass<out GencyclerDataContainer>)



