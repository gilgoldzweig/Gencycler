package goldzweigapps.com.annotations.annotations

import kotlin.reflect.KClass


/**
 * Informs the Gencycler code generator what viewHolders it wants to have
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
annotation class GencyclerAdapter(vararg val value: KClass<out GencyclerModel>,
								  val customName: String = "")



