package goldzweigapps.com.gencycler.kotlin

import android.content.Context
import goldzweigapps.com.annotations.annotations.*
import goldzweigapps.com.gencycler.R
import goldzweigapps.com.gencycler.recyclerAdapters.CustomKotlinAdapterName

@RecyclerAdapter("CustomKotlinAdapterName")
class KotlinTestAdapter(context: Context,
                        elements: ArrayList<GencyclerDataType>) : CustomKotlinAdapterName(context, elements) {

    override fun ProfileTypeViewHolder.onBindProfileTypeViewHolder(position: Int, element: ProfileType) {
    //The Function is a extension function of the view holder it has all the views in it
    }
}