package goldzweigapps.com.gencycler.kotlin

import goldzweigapps.com.annotations.annotations.GencyclerDataType
import goldzweigapps.com.annotations.annotations.Holder
import goldzweigapps.com.gencycler.R


@Holder(R.layout.kotlin_profile_type, KotlinTestAdapter::class)
data class ProfileType(
        val name: String,
        val age: Int,
        val profilePicture: String) : GencyclerDataType