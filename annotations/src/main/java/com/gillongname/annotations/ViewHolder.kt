package com.gillongname.annotations

/**
 * Requests the Gencycler code generator to generate a new ViewHolder for the annotated class
 *
 * @param  The layout resource id(R.layout.some_layout_per_item)
 *
 * for example
 *
 *	   <TextView
android:id="@+id/profile_item_name_txt"
android:layout_width="wrap_content"
android:layout_height="wrap_content"/>
 *
 * Will be generated as
 *
 * 		val profileItemNameTxt: TextView = findView(R.id.profile_item_name_txt)
 *
 * @see GencyclerModel
 * @see Adapter
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD)
@Repeatable
annotation class ViewHolder

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD)
@Repeatable
annotation class LifecycleViewHolder(val value: Int)
