package com.gillongname.core

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Custom open class used for by the generated ViewHolder
 *
 * @param view The inflated view provided in the generation process
 */
open class GencyclerHolder(view: View) : RecyclerView.ViewHolder(view) {
    protected fun <T : View> findView(@IdRes id: Int): T = itemView.findViewById(id)
}
