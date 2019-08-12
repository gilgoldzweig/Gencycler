package goldzweigapps.com.sample.activities.adapters

import android.content.Context
import goldzweigapps.com.annotations.annotations.GencyclerAdapter
import goldzweigapps.com.gencycler.RestaurantItemViewHolder
import goldzweigapps.com.sample.models.RestaurantItem

@GencyclerAdapter(RestaurantItem::class)
class RestaurantAdapter(context: Context) : GeneratedRestaurantAdapter(context) {

    override fun onBindRestaurantItemViewHolder(
        restaurantItemViewHolder: RestaurantItemViewHolder,
        restaurantItem: RestaurantItem,
        position: Int
    ) {
        with(restaurantItemViewHolder) {
            itemMobilePaymentRestaurantName.text = restaurantItem.name
            itemMobilePaymentRestaurantAddress.text = restaurantItem.address
            itemMobilePaymentRestaurantDistance.text = restaurantItem.distance
        }
    }

    override fun onRecycledRestaurantItemViewHolder(
        restaurantItemViewHolder: RestaurantItemViewHolder,
        position: Int
    ) {
        super.onRecycledRestaurantItemViewHolder(restaurantItemViewHolder, position)
        with(restaurantItemViewHolder) {
            itemMobilePaymentRestaurantName.text = null
            itemMobilePaymentRestaurantAddress.text = null
            itemMobilePaymentRestaurantDistance.text = null
        }
    }
}