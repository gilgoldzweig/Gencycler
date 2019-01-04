package goldzweigapps.com.annotations.annotations

/**
 * Informs the Gencycler code generator that you want the adapter to generate filterable functions
 *
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class Filterable