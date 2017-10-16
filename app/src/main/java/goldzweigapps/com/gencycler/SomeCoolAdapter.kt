package goldzweigapps.com.gencycler

import android.content.Context
import android.view.View
import android.widget.*
import goldzweigapps.com.annotations.*
import goldzweigapps.com.annotations.annotations.GencyclerAdapter
import goldzweigapps.com.annotations.annotations.GencyclerHolder
import goldzweigapps.com.annotations.annotations.GencyclerViewType
import goldzweigapps.com.annotations.interfaces.GencyclerDataType
import goldzweigapps.com.gencycler.SomeCoolAdapter
import goldzweigapps.com.gencycler.adapters.MyAdapterCustomName


/**
 * Created by gilgoldzweig on 14/10/2017.
 */


const val typeOne = "goldzweigapps.com.gencycler.TypeOne"
const val typeTwo = "goldzweigapps.com.gencycler.TypeTwo"
const val typeThree = "goldzweigapps.com.gencycler.TypeThree"


@GencyclerAdapter(
        GencyclerHolder(R.layout.type_one,
                GencyclerViewType("typeOne", R.id.type_one_one_text, TextView),
                GencyclerViewType("typeTwo", R.id.type_one_two_text, TextView),
                GencyclerViewType("typeThree", R.id.type_one_three_text, TextView),
                classType = typeOne),

        GencyclerHolder(R.layout.type_two,
                GencyclerViewType("typeOneText", R.id.type_two_one_text, TextView),
                GencyclerViewType("typeTwoText", R.id.type_two_three_text, TextView),
                classType = typeTwo),

        GencyclerHolder(R.layout.type_three,
                GencyclerViewType("typeOne", R.id.type_three_one_text, TextView),
                GencyclerViewType("typeTwo", R.id.type_three_two_text, ImageView),
                GencyclerViewType("typeThree", R.id.type_three_three_text, RadioButton),
                classType = typeThree),
        customName = "MyAdapterCustomName")

class SomeCoolAdapter(context: Context, elements: ArrayList<GencyclerDataType>) :
        MyAdapterCustomName(context, elements) {

    override fun onBindTypeOneViewHolder(typeOne: TextView,
                                         typeTwo: TextView,
                                         typeThree: TextView,
                                         rootView: View, holder: TypeOneViewHolder,
                                         position: Int, element: TypeOne) {
    }

    override fun onBindTypeTwoViewHolder(typeOneText: TextView,
                                         typeTwoText: TextView,
                                         rootView: View, holder: TypeTwoViewHolder,
                                         position: Int, element: TypeTwo) {

    }

    override fun onBindTypeThreeViewHolder(typeOne: TextView,
                                           typeTwo: ImageView,
                                           typeThree: RadioButton,
                                           rootView: View, holder: TypeThreeViewHolder,
                                           position: Int, element: TypeThree) {

    }

}
