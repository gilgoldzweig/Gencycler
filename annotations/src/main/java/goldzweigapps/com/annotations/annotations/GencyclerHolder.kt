package goldzweigapps.com.annotations.annotations

/**
 * Created by gilgoldzweig on 12/10/2017.
 */
@Retention(AnnotationRetention.SOURCE)
annotation class GencyclerHolder(val layoutResId: Int,
                                 vararg val viewTypes: GencyclerViewType,
                                 val classType: String)