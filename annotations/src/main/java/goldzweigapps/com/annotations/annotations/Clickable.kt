package goldzweigapps.com.annotations.annotations

/**
 * Informs the Gencycler code generator that you want to be informed when an item is clicked
 * this will modify the constructor
 *
 * @see GencyclerViewHolder
 * @see LongClickable for the long click listener
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class Clickable