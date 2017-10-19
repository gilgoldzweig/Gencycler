# Gencycler

### Gencycler is an compile time annotation processor, that writes the RecyclerView adapter code for you.

The adpater gives you a thread safe way to access the elements just like a list.
It does that by using extension functions like add, in(contains), remove, and much more(Look below to see full function list)
And it's written competently, in kotlin(<3).

### Usage
In order to use it your data types must implement an empty interface,
`GencyclerDataType`
The class let's the adapter identify between multiple view types and determine which is needed to be inflated.

In order for use to decide when each type is inflated at what time you pass a list of the interface.


##### I'm using 'data class' but it could be anything that implements the interface.
    data class PersonType(val name: String = "Rick Sanchez", val age: Int = 1000, val homeLocation: String = "Hahahahah"): GencyclerDataType

##### And another class just to have better example
    
    
    data class AdType(val adId: String, val adType: String): GencyclerDataType

#### Now to the actual adapter

        @GencyclerAdapter( 
            GencyclerViewHolder(R.layout.person_layout, //the layout res you want to be created 
            
                GencyclerViewField("name", //fieldName 
                                   R.id.type_one_one_text, // view's id
                                   AppCompatTextView::class), // view's type
                          
                GencyclerViewField("age",
                                   R.id.age_text,
                                   AppCompatTextView::class), //same as above
                
                GencyclerViewField("homeLocation",
                                   R.id.home_location_text,
                                   AppCompatImageView::class), //same as above
                                   
                classType = PersonType::class),
                //the class you want to use when the holder is binded
        
         //another view holder
         GencyclerViewHolder(R.layout.ad_layout,
        
                GencyclerViewField("adTitle",
                                   R.id.ad_title_text,
                                   AppCompatTextView::class),
                
                GencyclerViewField("adPreview",
                                   R.id.ad_preview_image,
                                   AppCompatImageView::class),
                                   
                classType = AdType::class),
        customName = ""
        // You can define a custom name for your generated adapter, if nothing is provided the name would be the same as your class name with a "Gencycler" prefix, example: "GencyclerYourAdapter")

    class YourAdapter(val context: Context, val elements: ArrayList<GencyclerDataType>) `

#### Build your app and the adapter will be generated to use it just extend it


    class YourAdapter(context: Context, elements: ArrayList<GencyclerDataType>): GencyclerYourAdapter(context, elements) {

    //for each of your view types you'll need to implement the customized onBind
                the method is an extension function of the view type you provided
                all of your views are there

                override fun PersonTypeViewHolder
                        .onBindPersonTypeViewHolder(position: Int, element: ProfileType) {
                  // use it however you like,
                  it could be simple as below or with a complex business logic. Your adapter your rules.
                   
                    name.text = element.name
                    age.text = element.age
                    homeLocation = element.homeLocation

                    //You're also provided with an onClick and OnLongClick functions for easier usage.
                    
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

##### When you extend the adapter you receive the following open functions.


`val customRecyclerAdapter = GencyclerYourAdapter(context, ArrayList())`

`customRecyclerAdapter - adType
//removing item and notifying the adapter if you are on uiThread`
    
`customRecyclerAdapter - 2 //removing item and notifying the adapter if you are on uiThread`
    
`customRecyclerAdapter + adType //adding item and notifying the adapter if you are on uiThread`

`customRecyclerAdapter.add(personType, 5)`

`customRecyclerAdapter[personType] //getPosition by type`

`customRecyclerAdapter[12] //getType by position`

`customRecyclerAdapter - 1..4 //removing position range and notifying the adapter if you are on uiThread`

`customRecyclerAdapter - listOf(adType, personType) //removing list of types and notifying the adapter if you are on uiThread`

`customRecyclerAdapter.setItem(5, newPersonType) // replaces the type in position 5 to newPersonType`

`customRecyclerAdapter.clear() // removes all the items and notifying the adapter if you are on uiThread`

`customRecyclerAdapter.count() // returns the list count `

`customRecyclerAdapter.setItems(listOf(personType, adType, adType, personType, morganFreemanPersonType)) //replace the current list and notifying the adapter if you are on uiThread`

`customRecyclerAdapter.isEmpty()`

`customRecyclerAdapter.isNotEmpty()`

 More functions and fetures will come in the feutre follow me on github and receive updates as soon as i make them.


MIT License

Copyright (c) 2017 Gil Goldzweig

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.



