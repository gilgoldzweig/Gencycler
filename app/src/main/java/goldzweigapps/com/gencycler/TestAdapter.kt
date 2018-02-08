package goldzweigapps.com.gencycler

import android.content.Context
import goldzweigapps.com.annotations.annotations.GencyclerAdapter
import goldzweigapps.com.annotations.annotations.experimental.GencyclerHolder
//import android.support.v7.widget.AppCompatImageView
//import android.support.v7.widget.AppCompatTextView
//import goldzweigapps.com.annotations.annotations.GencyclerAdapter
//import goldzweigapps.com.annotations.annotations.GencyclerAdapterBeta
//import goldzweigapps.com.annotations.annotations.GencyclerViewHolder
//import goldzweigapps.com.annotations.annotations.GencyclerViewField
//import goldzweigapps.com.annotations.annotations.experimental.GencyclerHolder
import goldzweigapps.com.annotations.interfaces.GencyclerDataType
import goldzweigapps.com.gencycler.adapters.GeneratedSomeClass

//import goldzweigapps.com.gencycler.adapters.GencyclerBrandNewAdpapter


/**
 * Created by gilgoldzweig on 14/10/2017.
 */

@GencyclerAdapter(R::class)
class SomeClass(context: Context, elements: ArrayList<GencyclerDataType>): GeneratedSomeClass(context, elements) {
    override fun ProfileTypeViewHolder.onBindProfileTypeViewHolder(position: Int, element: ProfileType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
//class TestAdapter(context: Context, elements: ArrayList<GencyclerDataType>): TestAdapter(context, elements) {
//    override fun ProfileTypeViewHolder.onBindProfileTypeViewHolder(position: Int, element: ProfileType) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun TypeFourViewHolder.onBindTypeFourViewHolder(position: Int, element: TypeFour) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//}


@GencyclerHolder("type_one", SomeClass::class)
data class ProfileType(
        val name: String,

        val age: Int,

        val profilePicture: String): GencyclerDataType