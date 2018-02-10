package goldzweigapps.com.gencycler

import android.content.Context
import goldzweigapps.com.annotations.annotations.GencyclerDataType
import goldzweigapps.com.annotations.annotations.Holder
import goldzweigapps.com.annotations.annotations.RecyclerAdapter
import goldzweigapps.com.gencycler.recyclerAdapters.TestAdapter


/**
 * Created by gilgoldzweig on 14/10/2017.
 */

@RecyclerAdapter(R::class, "TestAdapter")
class SomeClass(context: Context, elements: ArrayList<GencyclerDataType>) : TestAdapter(context, elements) {
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


@Holder("type_one", SomeClass::class)
data class ProfileType(
        val name: String,

        val age: Int,

        val profilePicture: String) : GencyclerDataType