package goldzweigapps.com.gencycler

import android.content.Context
import goldzweigapps.com.annotations.annotations.GencyclerDataType
import goldzweigapps.com.annotations.annotations.Holder
import goldzweigapps.com.annotations.annotations.RecyclerAdapter
import goldzweigapps.com.annotations.annotations.UniqueString
import goldzweigapps.com.gencycler.recyclerAdapters.TestAdapter
import goldzweigapps.com.gencycler.recyclerAdapters.TestAdapter2


@RecyclerAdapter("TestAdapter")
class SomeClass(context: Context, elements: ArrayList<GencyclerDataType>) : TestAdapter(context, elements) {
    override fun ProfileType2ViewHolder.onBindProfileType2ViewHolder(position: Int, element: ProfileType2) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun ProfileTypeViewHolder.onBindProfileTypeViewHolder(position: Int, element: ProfileType) {

    }
}

@RecyclerAdapter("TestAdapter2")
class SomeClass2(context: Context, elements: ArrayList<GencyclerDataType>) : TestAdapter2(context, elements) {
    override fun ProfileTypeViewHolder.onBindProfileTypeViewHolder(position: Int, element: ProfileType) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun ProfileType2ViewHolder.onBindProfileType2ViewHolder(position: Int, element: ProfileType2) {

    }
}


@Holder(R.layout.type_two, SomeClass::class, SomeClass2::class, uniqueString = "Gil")
data class ProfileType(
        @UniqueString
        val name: String,

        val age: Int,

        val profilePicture: String) : GencyclerDataType


@Holder(R.layout.type_one, SomeClass2::class, SomeClass::class)
data class ProfileType2(
        val name: String,

        val age: Int,

        val profilePicture: String) : GencyclerDataType