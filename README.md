# Gencycler

### Gencycler works in compile time so no runtime performance impact,
Gencycler will generated a readable multi view type RecyclerView Adapter with a thread-safe accessing mechanism.
Gencycler eliminates The need to write all of the boilerplate code needed for writing an adapter and
leaves you with only the bare minimum needed to write your business logic.

By iterating over the xml files and parsing the views it removes all of the need to write findViewById

### Install

#### Kotlin
     apply plugin: 'kotlin-kapt'

      repositories {
         maven { url "https://jitpack.io" }
      }
      dependencies {
            compile 'com.github.gilgoldzweig.Gencycler:annotations:latest_version'
            kapt 'com.github.gilgoldzweig.Gencycler:compiler:latest_version'
      }
#### Java
    apply plugin: 'kotlin-android'

    repositories {
      maven { url "https://jitpack.io" }
    }

    dependencies {
         compile "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$latest_kotlin_version"
         compile 'com.github.gilgoldzweig.Gencycler:annotations:latest_version'
         annotationProcessor 'com.github.gilgoldzweig.Gencycler:compiler:latest_version'
    }
### Usage
The usage is separated into two simple parts

#### Adapter
The adapter class needs to add the `@RecyclerAdapter` annotation above the adapter,
in addition the generated adapter will need to receive two required parameters `Context, ArrayList<GencyclerDataType>`

##### Kotlin
    @RecyclerAdapter("CustomKotlinAdapterName")
    //customName is optional if not present the name will be "Gencycler + Your ClassName"

     class KotlinTestAdapter(context: Context,
                         elements: ArrayList<GencyclerDataType>)

##### Java
    @RecyclerAdapter(customName = "CustomJavaAdapterName")
     //customName is optional if not present the name will be "Gencycler + Your ClassName"
    public class JavaTestAdapter {
        public JavaTestAdapter(Context context, ArrayList<GencyclerDataType> elements) {
        }
    }

#### Holder
Each holder represent a data type with whatever data it will contain
In order for a class to be considered a Holder it needs to do two things

 1. You must implement an empty interface named `GencyclerDataType`,
    that way the adapter can decide what type correspond to which layout

 2. The class needs to add the `@Holder` annotation above itself,

##### Kotlin
    @Holder(R.layout.kotlin_profile_type, //The layout you want to be used for the type
            KotlinTestAdapter::class) // The adapters you want to have the type
     data class ProfileType(
             val name: String,
             val age: Int,
             val profilePicture: String) : GencyclerDataType

##### Java
    @Holder(layoutRes = R.layout.java_profile_type, //The layout you want to be used for the type
             recyclerAdapters = {JavaTestAdapter.class}) // The adapters you want to have the type
     public class JavaProfileType implements GencyclerDataType {
         private String name;
         private int age;
         private String profilePicture;

         public JavaProfileType(String name, int age, String profilePicture) {
             this.name = name;
             this.age = age;
             this.profilePicture = profilePicture;
         }
     }


#### Build your app and the adapter will be generated
All you need to do is extend the generated adapter and implement the onBind function per holder

##### Kotlin
    @RecyclerAdapter("CustomKotlinAdapterName")
     class KotlinTestAdapter(context: Context,
                             elements: ArrayList<GencyclerDataType>) : CustomKotlinAdapterName(context, elements) {

         override fun ProfileTypeViewHolder.onBindProfileTypeViewHolder(position: Int, element: ProfileType) {
                //The Function is a extension function of the view holder
                //it has all the views from the layout file that have an (android:id="@+id/your_id") attribute

         }
     }
##### Java
    @RecyclerAdapter(customName = "CustomJavaAdapterName")
     public class JavaTestAdapter extends CustomJavaAdapterName {
         public JavaTestAdapter(Context context, ArrayList<GencyclerDataType> elements) {
             super(context, elements);
         }

         @Override
         public void onBindJavaProfileTypeViewHolder(@NotNull JavaProfileTypeViewHolder holder,
                                                     int position,
                                                     @NotNull JavaProfileType element) {
                    //The Function has the custom ViewHolder as a parameter
                    //it contains all the views from the layout file that have an (android:id="@+id/your_id") attribute
         }
     }



#### What going on in the background
The processor will find the location of the xml file per holder and parse it to find all of it's views
If the view does not have an (android:id="@+id/your_id") attribute it will be ignored
The Generated adapter looks something like this

    abstract class CustomJavaAdapterName(val context: Context, elements: ArrayList<GencyclerDataType> = ArrayList()) : GencyclerAdapterExtensions(elements) {
        private val inflater: LayoutInflater = LayoutInflater.from(context)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = when(viewType) {
                R.layout.java_profile_type -> JavaProfileTypeViewHolder(inflater.inflate(viewType, parent, false))
                else -> throw IOException("unsupported type, only (JavaProfileType) are supported")
        }
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
           when (holder) {
           is JavaProfileTypeViewHolder ->
                   holder.onBindJavaProfileTypeViewHolder(position, elements[position] as JavaProfileType)

           }
        }

        override fun getItemCount(): Int = elements.size

        override fun getItemViewType(position: Int): Int {
           val element = elements[position]
           return when {

                   element is JavaProfileType -> R.layout.java_profile_type
                   else -> throw IOException("unsupported type at $position, only (JavaProfileType) are supported")
           }}

        abstract fun JavaProfileTypeViewHolder.onBindJavaProfileTypeViewHolder(position: Int, element: JavaProfileType)

        class JavaProfileTypeViewHolder(view: View) : ViewHolder(view) {
           val typeOneOneText: TextView = view.findViewById(R.id.type_one_one_text)

           val subroot: LinearLayout = view.findViewById(R.id.subroot)

           val typeSubOneTwoText: TextView = view.findViewById(R.id.type_sub_one_two_text)

           val typeSubOneThreeText: TextView = view.findViewById(R.id.type_sub_one_three_text)

           val typeOneTwoText: TextView = view.findViewById(R.id.type_one_two_text)

           val typeOneThreeText: TextView = view.findViewById(R.id.type_one_three_text)

           val typeOneFourText: TextView = view.findViewById(R.id.type_one_four_text)
        }
     }


##### When you extend the adapter you also get an array of functions
In kotlin the functions will most likely be operators and in java they will be just like normal functions

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

 More functions and features will come in the future follow me on GitHub and receive updates as soon as I make them.


`MIT License

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
SOFTWARE.`
