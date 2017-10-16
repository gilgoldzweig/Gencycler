package goldzweigapps.com.annotations.annotations

/**
 * Created by gilgoldzweig on 12/10/2017.
 *
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class GencyclerAdapter(vararg val holders: GencyclerHolder, val customName: String = "")