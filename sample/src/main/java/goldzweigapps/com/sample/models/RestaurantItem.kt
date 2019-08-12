package goldzweigapps.com.sample.models

import goldzweigapps.com.annotations.annotations.GencyclerModel
import goldzweigapps.com.annotations.annotations.GencyclerViewHolder
import goldzweigapps.com.sample.R

@GencyclerViewHolder(R.layout.item_restaurant)
data class RestaurantItem(
    var name: String = "",

    var logoUrl: String = "",

    var address: String = "",

    var distance: String
): GencyclerModel