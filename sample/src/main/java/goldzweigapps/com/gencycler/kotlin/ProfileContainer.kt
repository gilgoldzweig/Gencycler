package goldzweigapps.com.gencycler.kotlin

import goldzweigapps.com.annotations.annotations.GencyclerDataContainer
import goldzweigapps.com.annotations.annotations.GencyclerViewHolder
import goldzweigapps.com.gencycler.R


@GencyclerViewHolder(R.layout.kotlin_profile_type)
data class ProfileContainer(
        val name: String,
        val age: Int,
        val profilePicture: String) : GencyclerDataContainer

@GencyclerViewHolder(R.layout.item_empty)
data class Profile2Container(
        val name: String,
        val age: Int,
        val profilePicture: String): GencyclerDataContainer

@GencyclerViewHolder(R.layout.kotlin_profile_type)
data class Profile3Container(
        val name: String,
        val age: Int,
        val profilePicture: String) : GencyclerDataContainer