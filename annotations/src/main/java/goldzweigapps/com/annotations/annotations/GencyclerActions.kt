package goldzweigapps.com.annotations.annotations

import kotlin.reflect.KClass


/**
 * Informs the Gencycler code generator what events it should generate listeners and for what types
 *
 * @param value our data containers which extends [GencyclerModel]
 * and they are annotated with the [GencyclerViewHolder] annotation
 *
 * @see GencyclerViewHolder
 * @see GencyclerModel
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
@Repeatable
@MustBeDocumented
annotation class GencyclerActions(val value: Array<Actions>)



enum class Actions {
    CLICK, LONG_CLICK, TOUCH, FILTER
}