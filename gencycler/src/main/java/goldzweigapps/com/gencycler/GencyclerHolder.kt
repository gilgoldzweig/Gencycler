package goldzweigapps.com.gencycler

import android.support.annotation.CallSuper
import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View

/**
 * Custom open class used for by the generated ViewHolder
 *
 * @param view The inflated view provided in the generation process
 */
open class GencyclerHolder(view: View) : RecyclerView.ViewHolder(view) {

 
    protected fun <T : View> findView(@IdRes id: Int): T = itemView.findViewById(id)


    open fun onClicked(onClicked: (view: View) -> Unit) {
        itemView.setOnClickListener(onClicked::invoke)
    }

    open fun onLongClicked(onLongClicked: (view: View) -> Boolean) {
        itemView.setOnLongClickListener(onLongClicked::invoke)
    }

    open fun onTouch(onTouch: (view: View, even: MotionEvent) -> Boolean) {
        itemView.setOnTouchListener(onTouch::invoke)
    }

    fun recycle() {
        itemView.setOnTouchListener(null)
        itemView.setOnClickListener(null)
        itemView.setOnLongClickListener(null)
    }

}


