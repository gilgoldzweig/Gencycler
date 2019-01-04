package goldzweigapps.com.annotations.annotations

import android.support.annotation.LayoutRes

/**
 * Requests the Gencycler code generator to generate a new ViewHolder for the annotated class
 *
 * @param value The layout resource id(R.layout.some_layout_per_item)
 *
 * @param namingCase By default the generator converts the view's id to a variable name in LowerCamelCase,
 * this provides you with other naming options
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
 * @see NamingCase
 * @see GencyclerModel
 * @see GencyclerAdapter
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
@Repeatable
@MustBeDocumented
annotation class GencyclerViewHolder(@LayoutRes val value: Int,
									 val namingCase: NamingCase = NamingCase.NAMING_CASE_CAMEL)



