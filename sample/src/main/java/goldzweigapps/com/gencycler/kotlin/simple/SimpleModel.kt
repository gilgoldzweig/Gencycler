package goldzweigapps.com.gencycler.kotlin.simple

import goldzweigapps.com.annotations.annotations.GencyclerModel
import goldzweigapps.com.annotations.annotations.GencyclerViewHolder
import goldzweigapps.com.annotations.annotations.NamingCase
import goldzweigapps.com.gencycler.R


@GencyclerViewHolder(R.layout.kotlin_profile_type)
data class SimpleModel(val name: String,
					   val description: String) : GencyclerModel

@GencyclerViewHolder(R.layout.kotlin_profile_type)
data class AnotherSimpleModel(val name: String,
					   val description: String) : GencyclerModel
