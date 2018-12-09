package goldzweigapps.com.annotations.annotations

/**
 * Informs the Gencycler code generator that you want to be informed when an item is long clicked
 * this will modify the constructor
 *
 * @see GencyclerViewHolder
 * @see Clickable for the regular click listener
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class LongClickable