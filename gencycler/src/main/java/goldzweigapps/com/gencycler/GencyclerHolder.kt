package goldzweigapps.com.gencycler

import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Custom open class used for by the generated ViewHolder
 *
 * @param view The inflated view provided in the generation process
 */
open class GencyclerHolder(view: View) : RecyclerView.ViewHolder(view) {

 
    fun <T : View> findView(@IdRes id: Int): T = itemView.findViewById(id)
}
