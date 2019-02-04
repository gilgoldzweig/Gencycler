package goldzweigapps.com.gencycler.kotlin.simple

import goldzweigapps.com.annotations.annotations.*
import goldzweigapps.com.gencycler.R


@GencyclerViewHolder(R.layout.kotlin_profile_type)
data class SimpleModel(val name: String,
					   val description: String) : GencyclerModel

@GencyclerViewHolder(R.layout.empty_test)
@GencyclerActions([Actions.CLICK, Actions.LONG_CLICK])
data class AnotherSimpleModel<T : CharSequence, K: Any>(val name: String,
												val description: String,
												val t: T) : GencyclerModel
