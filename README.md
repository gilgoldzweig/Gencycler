# Gencycler

Gencycler is an compile time annotation processor,
that writes the RecyclerView adapter code for you and gives you a thread safe way to access the adapter just like a list,
using extension functions like add, in(contains), remove, and much more(Look below to see full function list)
And it's written competently, in kotlin(<3).

### Usage
In order to use it your data types must implement an empty interface,
which let the adapter identify between multiple view types and determine which needed to be inflated
in order for use to decide when each type is inflated at what time you pass a list of the interface

`
//I'm using data class but it could be anything that implements the interface
data class PersonType(val name: String, val age: Int, val homeLocation: String = "Nope"): GencyclerDataType

//Lets make another class here

data class AdType(val adId: String, val adType: String): GencyclerDataType
`
Now to the actual adapter
`
@GencyclerAdapter(
        GencyclerViewHolder(R.layout.person_layout,//the layout res you want to be created
                GencyclerViewField("name", //fieldName
                                   R.id.type_one_one_text, // view's res id
                                   AppCompatTextView::class // view's type
                                   ),
                GencyclerViewField("age", R.id.age_text, AppCompatTextView::class), //same as above
                GencyclerViewField("homeLocation", R.id.home_location_text, AppCompatImageView::class), //same as above
                classType = PersonType::class //the class you want to use when the holder is binded
                ),

        //another view holder
        GencyclerViewHolder(R.layout.ad_layout,
                GencyclerViewField("adTitle", R.id.ad_title_text, AppCompatTextView::class),
                GencyclerViewField("adPreview", R.id.ad_preview_image, AppCompatImageView::class),
                classType = AdType::class),
                customName = "" // You can define a custom name for your generated adapter if nothing is provided
                the name would be the same as your class name with a "Gencycler" prefix
                example: "GencyclerYourAdapter"
                )
class YourAdapter(val context: Context, val elements: ArrayList<GencyclerDataType>)

Build your app and the adapter will be generated to use it just extend it


class YourAdapter(context: Context, elements: ArrayList<GencyclerDataType>):
            GencyclerYourAdapter(context, elements) {

            //for each of your view types you'll need to implement the customized onBind
                the method is an extension function of the view type you provided
                all of your views are there

            override fun PersonTypeViewHolder
                        .onBindPersonTypeViewHolder(position: Int, element: ProfileType) {

                  // use it how ever you like, it could be simple as below or with complex business logic
                    your adapter your rules

                    name.text = element.name
                    age.text = element.age
                    homeLocation = element.homeLocation

                    //you're provided with onClick and OnLongClick function for easier usage
                    onClick {
                    }


                    onLongClick {

                    }
                }

                override fun AdTypeViewHolder
                        .onBindAdTypeViewHolder(position: Int, element: AdType) {
                        //same as above
                }

}
when you extend the adapter you receive the following open functions


val customRecyclerAdapter = CustomRecyclerAdapter(this, ArrayList())

customRecyclerAdapter - adType //removing item and notifying the adapter if you are on uiThread

customRecyclerAdapter - 2 //removing item and notifying the adapter if you are on uiThread

customRecyclerAdapter + adType //adding item and notifying the adapter if you are on uiThread

customRecyclerAdapter.add(personType,5)

customRecyclerAdapter[personType] //getItem by object

customRecyclerAdapter[12] //getItem by position

customRecyclerAdapter - (1..4) //removing position range and notifying the adapter if you are on uiThread

customRecyclerAdapter - (listOf(adType, personType)) //removing list of objects and notifying the adapter if you are on uiThread

customRecyclerAdapter.setItem(5, newPersonType)

customRecyclerAdapter.clear()

customRecyclerAdapter.count()

customRecyclerAdapter.setItems(listOf(personType, adType, adType, personType, morganFreemanPersonType)) //replace the current list and notifying the adapter if you are on uiThread

customRecyclerAdapter.isEmpty()

customRecyclerAdapter.isNotEmpty()
`

