package goldzweigapps.com.gencycler

import goldzweigapps.com.annotations.annotations.experimental.GencyclerHolder
import goldzweigapps.com.annotations.interfaces.GencyclerDataType

/**
 * Created by gilgoldzweig on 14/10/2017.
 */
@GencyclerHolder("type_one", SomeClass::class)
data class ProfileType(
        val name: String,

        val age: Int,

        val profilePicture: String): GencyclerDataType

//@GencyclerHolder("type_two", SomeClass::class)
class TypeFour : GencyclerDataType